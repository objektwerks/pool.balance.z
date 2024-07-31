package objektwerks

import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}
import zio.http.{handler, Method, Request, Response, Routes}
import zio.json.{DecoderOps, EncoderOps}

import Serializer.given

object Server extends ZIOAppDefault:
  val routes: Routes[Any, Response] = Routes(
    Method.POST / "command" -> handler: (request: Request) =>
        request.body.asString.flatMap { json =>
          json.fromJson[Command] match
            case Right(command: Command) =>
              for
                _       <- ZIO.log(s"*** Command: $command")
                handler <- ZIO.service[Handler]
                event   <- handler
                            .handle(command)
                            .catchAll( throwable =>
                              val message = s"*** Failed to process command: $command due to this error: ${throwable.getMessage}"
                              ZIO.log(message) zip ZIO.succeed(Fault(message))
                            )
                _       <- ZIO.log(s"*** Event: $event")
              yield Response.json(event.toJson)
            case Left(error: String) => 
              val fault = Fault(error)
              ZIO.log(s"*** Fault: $fault") *> Response.json(fault.toJson)
        }
  ).handleError( _ match
    case error: String => Response.json( Fault(s"Invalid json: $error").toJson )
  )

  override def run: ZIO[Environment & (ZIOAppArgs & Scope ), Any, Any] =
    for
      conf   <- Resources.loadZIOConfig("server.conf")
      host   =  conf.getString("server.host")
      port   =  conf.getInt("server.port")
      ds     =  conf.getConfig("ds")
      email  =  conf.getConfig("email")
      config =  zio.http.Server.Config.default.binding(host, port)
      _      <- ZIO.log(s"*** Server running at http://$host:$port")
      server <- zio.http.Server
                  .serve(routes)
                  .provide(
                    Store.dataSourceLayer(ds),
                    Store.namingStrategyLayer,
                    Store.licenseCacheLayer,
                    Store.layer,
                    Emailer.layer(email),
                    Handler.layer,
                    ZLayer.succeed(config),
                    zio.http.Server.live
                  )
                  .debug
                  .exitCode
    yield server