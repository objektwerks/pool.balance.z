package objektwerks

import java.nio.file.Path

import zio.{Runtime, Scope, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}
import zio.http.{Server => HttpServer, ServerConfig}
import zio.logging.{LogFormat, file}

import Serializer.given

object Server extends ZIOAppDefault: 
  override val bootstrap: ZLayer[ZIOAppArgs, Any, Environment] =
    Runtime.removeDefaultLoggers >>> file( Path.of("~/.poolbalance.z/server.log") )

  override def run: ZIO[Environment & (ZIOAppArgs & Scope), Any, Any] =
    for
      config <- Resources.loadZIOConfig("server.conf")
      host   =  config.getString("host")
      port   =  config.getInt("port")
      _      <- ZIO.log(s"*** Server running at http://$host:$port")
    yield HttpServer
            .serve(Router.router)
            .provide(
              ServerConfig.live( ServerConfig.default.binding(host, port) ),
              HttpServer.live,
              Handler.layer,
              Store.layer,
              Store.dataSourceLayer,
              Store.namingStrategyLayer,
              Store.licenseCacheLayer
            )