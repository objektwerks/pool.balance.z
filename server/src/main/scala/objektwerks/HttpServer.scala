package objektwerks

import java.nio.file.Path
import java.time.Instant

import zio.{Runtime, Scope, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}
import zio.http.{!!, /, ->, Http, Request, Response, Server, ServerConfig}
import zio.http.model.Method
import zio.json.{DecoderOps, EncoderOps}
import zio.logging.{LogFormat, file}

object HttpServer extends ZIOAppDefault:
  val router: Http[Any, Throwable, Request, Response] = Http.collectZIO[Request] {
    case Method.GET -> !! / "now" => ZIO.succeed( Response.text(Instant.now.toString()) )
    case request @ Method.POST -> !! / "greeting" => request.body.asString.map { name => Response.text(s"\nGreetings, $name!\n") }
  }

  override val bootstrap: ZLayer[ZIOAppArgs, Any, Environment] =
    Runtime.removeDefaultLoggers >>> file(Path.of("./target/http-server.log"))

  override def run: ZIO[Environment & (ZIOAppArgs & Scope ), Any, Any] =
    for
      args   <- getArgs
      port   =  args.headOption.getOrElse("7272").toInt
      config =  ServerConfig.default.port(port)
      _      <- ZIO.log(s"HttpServer running at http://localhost:$port")
      server <- Server
                  .serve(router)
                  .provide(ServerConfig.live(config), Server.live)
                  .exitCode
    yield server