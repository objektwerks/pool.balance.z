package objektwerks

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import zio.json.{DecoderOps, EncoderOps}

import Serializer.given

object Proxy extends LazyLogging:
  val headers = Map (
    "Content-Type" -> "application/json; charset=utf-8",
    "Accept" -> "application/json"
  )

  def call(command: Command,
           handler: (either: Either[Fault, Event]) => Unit) =
    val event = post(command)
    handle(event, handler)

  private def post(command: Command): Future[Event] =
    logger.info(s"Proxy:post command: $command")
    Future( Fault("todo") )
    /*
    params.body = write[Command](command)
    logger.info(s"Proxy:post params: $params")
    (
      for
        response <- dom.fetch(Url.command, params)
        text     <- response.text()
      yield
        logger.info(s"Proxy:post text: $text")
        val event = read[Event](text)
        logger.info(s"Proxy:post event: $event")
        event
    ).recover {
      case failure: Exception =>
        logger.info(s"Proxy:post failure: ${failure.getCause}")
        Fault(failure)
    }
    */

  private def handle(future: Future[Event],
                     handler: (either: Either[Fault, Event]) => Unit): Unit =
    future map { event =>
      handler(
        event match
          case fault: Fault =>
            logger.info(s"Proxy:handle fault: $fault")
            Left(fault)
          case event: Event =>
            logger.info(s"Proxy:handle event: $event")
            Right(event)
      )
    }