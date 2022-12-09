package objektwerks

import java.nio.file.Path

import scala.sys

import zio.{Runtime, Scope, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}
import zio.http.ServerConfig
import zio.logging.{LogFormat, file}

import Serializer.given
import java.nio.file.Files
import java.nio.file.Paths

object Server extends ZIOAppDefault: 
  override val bootstrap: ZLayer[ZIOAppArgs, Any, Environment] =
    val serverDir =  s"${sys.props("user.home")}/.poolbalance.z"
    var serverPath = Paths.get(serverDir)
    if !Files.exists(serverPath) then serverPath = Files.createDirectory(serverPath)
    Runtime.removeDefaultLoggers >>> file( Path.of(s"$serverDir/server.log") )

  override def run =
    for
      config <- Resources.loadZIOConfig("server.conf")
      host   =  config.getString("host")
      port   =  config.getInt("port")
      _      <- ZIO.log(s"*** Server running at http://$host:$port")
    yield zio.http.Server
            .serve(Router.router)
            .provide(
              ServerConfig.live( ServerConfig.default.binding(host, port) ),
              zio.http.Server.live,
              Handler.layer,
              Store.layer,
              Store.dataSourceLayer,
              Store.namingStrategyLayer,
              Store.licenseCacheLayer
            )
            .exitCode