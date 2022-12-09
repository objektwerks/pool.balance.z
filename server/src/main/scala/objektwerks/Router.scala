package objektwerks

import zio.ZIO
import zio.http.{!!, /, ->, Http, Request, Response}
import zio.http.model.Method
import zio.json.{DecoderOps, EncoderOps}

import Serializer.given

object Router:
  val router: Http[Handler, Throwable, Request, Response] = Http.collectZIO[Request] {
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