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
  val port = 7272
  val config = ServerConfig.default.port(port)

  override val bootstrap: ZLayer[ZIOAppArgs, Any, Environment] =
    Runtime.removeDefaultLoggers >>> file(Path.of("./target/server.log"))

  def router: Http[Any, Throwable, Request, Response] = Http.collectZIO[Request] {
    case Method.GET -> !! / "now" => ZIO.succeed( Response.text(Instant.now.toString()) )
  }

  def app: ZIO[HttpServer, Nothing, Nothing] =
    for
      _      <- ZIO.log(s"HttpServer running at http://localhost:$port")
      server <- HttpServer.serve(router)
    yield server

  override def run: ZIO[Environment & (ZIOAppArgs & Scope ), Any, Any] =
    app
      .provide(ServerConfig.live(config), HttpServer.live)