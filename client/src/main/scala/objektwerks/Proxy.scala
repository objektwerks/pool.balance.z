package objektwerks

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.typesafe.scalalogging.LazyLogging

import zio.ZIO
import zio.http.{Body, Client, Request}

import Serializer.given

object Proxy extends LazyLogging:
  private val url = Context.url

  def call(command: Command,
           handler: Event => Unit): Unit =
    (
      for
        _           <- ZIO.succeed( logger.info(s"*** Proxy command: $command") )
        commandJson =  writeToString[Command](command)
        request     =  Request.post(url, Body.fromString(commandJson)) // Context.headers?
        response    <- Client.request(request)
        eventJson   <- response.body.asString.orDie
        event       =  readFromString[Event](eventJson)
        _           <- ZIO.succeed( logger.info(s"*** Proxy event: $event") ) *> ZIO.succeed( handler(event) )
      yield ()
    ).catchAll { error =>
        val fault = Fault(error.getMessage)
        for
          _ <- ZIO.succeed( logger.info(s"*** Proxy fault: $fault") ) *> ZIO.succeed( handler(fault) )
        yield ()
    }