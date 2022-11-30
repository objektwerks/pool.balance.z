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

  val router: Http[Handler, Throwable, Request, Response] = Http.collectZIO[Request] {
    case request @ Method.POST -> !! / "command" => request.body.asString.map { json =>
      json.fromJson[Command] match
        case Right(command) =>
          for
            handler <- ZIO.service[Handler]
            event   <- handler.handle(command)
          yield Response.json( event ) // event.toJson required, but fails!
        case Left(error) =>
          Response.json( Fault(error).toJson )
    }
  }.provideLayer(Handler.layer)

  override def run: ZIO[Environment & (ZIOAppArgs & Scope ), Any, Any] =
    for
      args   <- getArgs
      port   =  args.headOption.getOrElse("7272").toInt
      config =  ServerConfig.default.port(port)
      _      <- ZIO.log(s"Server running at http://localhost:$port")
      server <- HttpServer.serve(router).provide(ServerConfig.live(config), HttpServer.live)
    yield server