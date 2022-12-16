package objektwerks

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import zio.ZIO
import zio.http.{Body, Client}
import zio.http.model.{Header, Headers}
import zio.json.{DecoderOps, EncoderOps}

import Serializer.given

object Proxy extends LazyLogging:
  val conf = Resources.loadConfig("server.conf")
  val host = conf.getString("host")
  val port = conf.getInt("port")
  val url = s"http://$host:$port/command"

  val headers = Headers (
    Header("Content-Type", "application/json; charset=utf-8"),
    Header("Accept", "application/json")
  )

  def call(command: Command,
           handler: Event => Unit): Unit =
    logger.info(s"Proxy:call command: $command")
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