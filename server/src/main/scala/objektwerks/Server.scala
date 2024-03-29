package objektwerks

import java.nio.file.{Files, Path, Paths}

import scala.sys

import zio.{Runtime, Scope, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}
import zio.http.{!!, /, ->, Http, Request, Response, ServerConfig}
import zio.http.model.Method
import zio.json.{DecoderOps, EncoderOps}
import zio.logging.file

import Serializer.given

object Server extends ZIOAppDefault:
  val route: Http[Handler, Throwable, Request, Response] = Http.collectZIO[Request] {
    case request @ Method.POST -> !! / "command" => request.body.asString.flatMap { json =>
      json.fromJson[Command] match
        case Right(command) =>
          for
            _       <- ZIO.log(s"*** Router command: $command")
            handler <- ZIO.service[Handler]
            event   <- handler
                         .handle(command)
                         .catchAll(throwable =>
                            val message = s"*** Failed to process command: $command due to this error: ${throwable.getMessage}"
                            ZIO.log(message) zip ZIO.succeed(Fault(message))
                          )
            _       <- ZIO.log(s"*** Router event: $event")
          yield Response.json(event.toJson)
        case Left(error) => 
          val fault = Fault(error)
          ZIO.log(s"*** Router fault: $fault") *> ZIO.succeed(Response.json(fault.toJson))
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
      email  =  conf.getConfig("email")
      config =  ServerConfig.default.binding(host, port)
      _      <- ZIO.log(s"*** Server running at http://$host:$port")
      server <- zio.http.Server
                  .serve(route.withDefaultErrorResponse)
                  .provide(
                    Store.dataSourceLayer(ds),
                    Store.namingStrategyLayer,
                    Store.licenseCacheLayer,
                    Store.layer,
                    Emailer.layer(email),
                    Handler.layer,
                    ServerConfig.live(config),
                    zio.http.Server.live
                  )
                  .debug
                  .exitCode
    yield server