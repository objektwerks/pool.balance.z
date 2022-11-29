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

object Server extends ZIOAppDefault:
  override val bootstrap: ZLayer[ZIOAppArgs, Any, Environment] =
    Runtime.removeDefaultLoggers >>> file(Path.of("~/.poolbalance.z/server.log"))

  val router: Http[Any, Throwable, Request, Response] = Http.collectZIO[Request] {
    case Method.GET -> !! / "now" => ZIO.succeed( Response.text(Instant.now.toString()) )
    case request @ Method.POST -> !! / "command" => request.body.asString.map { json =>
      json.fromJson[Command] match
        case Right(command) => command match // TODO!
          case ListPools()                  => Response.json( PoolsListed(Nil).toJson )
          case SavePool(pool)               => Response.json( PoolSaved(pool).toJson )
          case ListCleanings()              => Response.json( CleaningsListed(Nil).toJson )
          case SaveCleaning(cleaning)       => Response.json( CleaningSaved(cleaning).toJson )
          case ListMeasurements()           => Response.json( MeasurementsListed(Nil).toJson )
          case SaveMeasurement(measurement) => Response.json( MeasurementSaved(measurement).toJson )
          case ListChemicals()              => Response.json( ChemicalsListed(Nil).toJson )
          case SaveChemical(chemical)       => Response.json( ChemicalSaved(chemical).toJson )
        case Left(error) => Response.json( Fault(error).toJson )
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