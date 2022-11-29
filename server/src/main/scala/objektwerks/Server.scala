package objektwerks

import java.nio.file.Path
import java.time.Instant

import zio.{Runtime, Scope, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}
import zio.http.{!!, /, ->, Http, Request, Response, ServerConfig}
import zio.http.{Server => HttpServer}
import zio.http.model.Method
import zio.json.{DecoderOps, EncoderOps}
import zio.logging.{LogFormat, file}

object Server extends ZIOAppDefault:
  override val bootstrap: ZLayer[ZIOAppArgs, Any, Environment] =
    Runtime.removeDefaultLoggers >>> file(Path.of("./target/server.log"))

  val router: Http[Any, Throwable, Request, Response] = Http.collectZIO[Request] {
    case Method.GET -> !! / "now" => ZIO.succeed( Response.text(Instant.now.toString()) )
  }

  override def run: ZIO[Environment & (ZIOAppArgs & Scope ), Any, Any] =
    for
      args   <- getArgs
      port   =  args.headOption.getOrElse("7272").toInt
      _      <- ZIO.log(s"HttpServer running at http://localhost:$port")
      config =  ServerConfig.default.port(port)
      server <- HttpServer.serve(router).provide(ServerConfig.live(config), HttpServer.live)
    yield server