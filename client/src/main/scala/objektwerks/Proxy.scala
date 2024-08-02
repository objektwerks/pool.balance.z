package objektwerks

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.typesafe.scalalogging.LazyLogging

import zio.{Unsafe, Runtime, ZIO}
import zio.http.{Body, Client, Request}

import Serializer.given

object Proxy extends LazyLogging:
  private val url = Context.url

  /* 
    val fault = Fault(error)
    ZIO.succeed( logger.info(s"*** Proxy fault: $fault") ) *>
    ZIO.succeed( handler(fault) )
   */

  private def delegate(command: Command,
                       handler: Event => Unit) =
    for
      _           <- ZIO.succeed( logger.info(s"*** Proxy command: $command") )
      commandJson =  writeToString[Command](command)
      request     =  Request.post(url, Body.fromString(commandJson)) // Context.headers?
      response    <- Client.request(request)
      eventJson   <- response.body.asString.orDie
      event       =  readFromString[Event](eventJson)
      _           <- ZIO.succeed( logger.info(s"*** Proxy event: $event") )
      _           <- ZIO.succeed( handler(event) )
    yield ()

  def call(command: Command,
           handler: Event => Unit): Unit =
    Unsafe.unsafe { implicit unsafe =>
      Runtime
        .default
        .unsafe
        .run( delegate(command, handler) )
        .exitCode
    }