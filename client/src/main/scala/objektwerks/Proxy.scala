package objektwerks

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import zio.{Unsafe, Runtime, ZIO}
import zio.http.{Body, Client}
import zio.json.{DecoderOps, EncoderOps}

import Context.*
import Serializer.given

object Proxy extends LazyLogging:
  private def delegate(command: Command,
                       handler: Event => Unit): ZIO[Any, Nothing, ZIO[Client, Throwable, Unit]] =
    ZIO.succeed(
      for
        _        <- ZIO.succeed( logger.info(s"*** Proxy command: $command") )
        response <- Client.request(
                      url = url,
                      headers = headers,
                      content = Body.fromString(command.toJson)
                    )
        _        <- response.body.asString.flatMap { json =>
                      json.fromJson[Event] match
                        case Right(event) =>
                          ZIO.succeed( logger.info(s"*** Proxy event: $event") ) *>
                          ZIO.succeed( handler(event) ).unit
                        case Left(error) =>
                          val fault = Fault(error)
                          ZIO.succeed( logger.info(s"*** Proxy fault: $fault") ) *>
                          ZIO.succeed( handler(fault) ).unit
                    }
      yield ()
    )

  def call(command: Command,
           handler: Event => Unit): Unit =
    Unsafe.unsafe { implicit unsafe =>
      Runtime
        .default
        .unsafe
        .run( delegate(command, handler) )
        .getOrThrowFiberFailure()
    }