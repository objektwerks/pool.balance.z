package objektwerks

import java.nio.file.{Files, Path, Paths}

import scala.sys

import zio.{Runtime, Scope, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}
import zio.http.{!!, /, ->, Http, Request, Response, Server, ServerConfig}
import zio.http.model.Method
import zio.json.{DecoderOps, EncoderOps}
import zio.logging.{LogFormat, file}

import Serializer.given

object Router extends ZIOAppDefault:
  val route: Http[Handler, Throwable, Request, Response] = Http.collectZIO[Request] {
    case request @ Method.POST -> !! / "command" => request.body.asString.flatMap { json =>
      json.fromJson[Command] match
        case Right(command) =>
          for
            handler <- ZIO.service[Handler]
            event   <- handler
                         .handle(command)
                         .catchAll(throwable =>
                            val message = s"*** Handler error: ${throwable.getMessage}; on: $command"
                            ZIO.log(message) zip ZIO.succeed(Fault(message))
                          )
          yield Response.json( event.toJson )
        case Left(error) => ZIO.succeed( Response.json( Fault(error).toJson ) )
    }
  }

  override val bootstrap: ZLayer[ZIOAppArgs, Any, Environment] =
    val serverDir =  s"${sys.props("user.home")}/.poolbalance.z"
    var serverPath = Paths.get(serverDir)
    if !Files.exists(serverPath) then serverPath = Files.createDirectory(serverPath)
    Runtime.removeDefaultLoggers >>> file( Path.of(s"$serverDir/server.log") )

  override def run: ZIO[Environment & (ZIOAppArgs & Scope ), Any, Any] =
    for
      conf   <- Resources.loadZIOConfig("server.conf")
      host   =  conf.getString("host")
      port   =  conf.getInt("port")
      ds     =  conf.getConfig("ds")
      config =  ServerConfig.default.binding(host, port)
      _      <- ZIO.log(s"*** Server running at http://$host:$port")
      server <- Server
                  .serve(route)
                  .provide(
                    ServerConfig.live(config),
                    Server.live,
                    Store.dataSourceLayer(ds),
                    Store.namingStrategyLayer,
                    Store.licenseCacheLayer,
                    Store.layer,
                    Handler.layer
                  )
                  .debug
                  .exitCode
    yield server