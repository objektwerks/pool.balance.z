package objektwerks

import java.nio.file.Path
import java.time.Instant

import zio.{Runtime, Scope, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}
import zio.http.{!!, /, ->, Http, Request, Response, ServerConfig}
import zio.http.{Server => HttpServer}
import zio.http.model.Method
import zio.json.{DecoderOps, EncoderOps}
import zio.logging.{LogFormat, file}

import Serializer.given
import com.typesafe.config.ConfigFactory

object Server extends ZIOAppDefault:
  val config = ConfigFactory.load("server.conf")
  val store = Store(config)
  val handler = Handler(store)

  override val bootstrap: ZLayer[ZIOAppArgs, Any, Environment] =
    Runtime.removeDefaultLoggers >>> file(Path.of("~/.poolbalance.z/server.log"))

  val router: Http[Any, Throwable, Request, Response] = Http.collectZIO[Request] {
    case Method.GET -> !! / "now" => ZIO.succeed( Response.text(Instant.now.toString()) )
    case request @ Method.POST -> !! / "command" => request.body.asString.map { json =>
      json.fromJson[Command] match
        case Right(command) => command match // TODO!
          case command @ ListPools()                  => Response.json( handler.handle(command).toJson )
          case command @ SavePool(pool)               => Response.json( handler.handle(command).toJson )
          case command @ ListCleanings()              => Response.json( CleaningsListed(Nil).toJson )
          case command @ SaveCleaning(cleaning)       => Response.json( CleaningSaved(0L).toJson )
          case command @ ListMeasurements()           => Response.json( MeasurementsListed(Nil).toJson )
          case command @ SaveMeasurement(measurement) => Response.json( MeasurementSaved(0L).toJson )
          case command @ ListChemicals()              => Response.json( ChemicalsListed(Nil).toJson )
          case command @ SaveChemical(chemical)       => Response.json( ChemicalSaved(0L).toJson )
        case Left(error) => Response.json( Fault( error ).toJson )
    }
  }

  override def run: ZIO[Environment & (ZIOAppArgs & Scope ), Any, Any] =
    for
      args   <- getArgs
      port   =  args.headOption.getOrElse("7272").toInt
      config =  ServerConfig.default.port(port)
      _      <- ZIO.log(s"Server running at http://localhost:$port")
      server <- HttpServer.serve(router).provide(ServerConfig.live(config), HttpServer.live)
    yield server