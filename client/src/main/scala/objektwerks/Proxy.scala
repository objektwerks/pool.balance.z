package objektwerks

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import zio.ZIO
import zio.http.{Body, Client}
import zio.json.{DecoderOps, EncoderOps}

import Context.*
import Serializer.given

object Proxy extends LazyLogging:
  def call(command: Command,
           handler: Event => Unit): Unit =
    logger.info(s"*** Proxy:call command: $command")
    for
      response <- Client.request(
                    url = url,
                    headers = headers,
                    content = Body.fromString(command.toJson)
                  )
      _        <- response.body.asString.flatMap { json =>
                    json.fromJson[Event] match
                      case Right(event) => ZIO.succeed( handler(event) ).unit
                      case Left(error) => ZIO.succeed( handler(Fault(error)) ).unit
                  }
    yield ()