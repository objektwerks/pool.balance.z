package objektwerks

import com.github.plokhotnyuk.jsoniter_scala.core.*

import zio.{Console, Scope, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}
import zio.http.{Charsets, handler, Method, Request, Response, Routes}

import Serializer.given

object Server extends ZIOAppDefault:
  val routes: Routes[Handler, Response] =
    Routes(
      Method.POST / "command" -> handler: (request: Request) =>
        for
          json    <- request.body.asString(Charsets.Utf8)
          _       <- Console.printLine(s"*** MediaType: ${request.body.mediaType.get.fullType} Json: $json")
          command =  readFromString[Command](json) // `unexpected end of input error` exception thrown here!
          _       <- Console.printLine(s"*** Command: $command")
          handler <- ZIO.service[Handler]
          event   <- handler.handle(command)
          _       <- Console.printLine(s"*** Event: $event")
        yield Response.json( writeToString[Event](event) )
    ).handleError( _ match
      case error: Throwable =>
        val fault = Fault(s"*** Fault: ${error.getMessage}")
        Console.printLine(s"*** $fault")
        val json  = writeToString[Event](fault)
        Response.json(json)
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