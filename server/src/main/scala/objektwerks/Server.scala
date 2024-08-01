package objektwerks

import zio.{Console, Scope, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}
import zio.http.{handler, Method, Request, Response, Routes}
import zio.json.{DecoderOps, EncoderOps}

import Serializer.given

object Server extends ZIOAppDefault:
  val routes = Routes(
    Method.POST / "command" -> handler: (request: Request) =>
      for
        body    <- request.body.asString.orDie
        _       <- Console.printLine(s"*** Json: $body")
        command <- ZIO.fromEither( body.fromJson[Command] )
        _       <- Console.printLine(s"*** Command: $command")
        handler <- ZIO.service[Handler]
        event   <- handler.handle(command)
        _       <- Console.printLine(s"*** Event: $event")
      yield Response.json(event.toJson)
  ).handleError( _ match
    case error: String => Response.json( Fault(s"*** Invalid json: $error").toJson )
    case _ => Response.json( Fault(s"*** Invalid json for unknow reason.").toJson )
  )

  override def run: ZIO[Environment & (ZIOAppArgs & Scope ), Any, Any] =
    for
      conf     <- Resources.loadZIOConfig("server.conf")
      host     =  conf.getString("server.host")
      port     =  conf.getInt("server.port")
      endpoint =  conf.getString("server.endpoint")
      ds       =  conf.getConfig("ds")
      email    =  conf.getConfig("email")
      config   =  zio.http.Server.Config.default.binding(host, port)
      _        <- ZIO.log(s"*** Server running at http://$host:$port$endpoint")
      server   <- zio.http.Server
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