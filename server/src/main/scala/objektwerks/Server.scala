package objektwerks

import java.nio.file.{Files, Path, Paths}

import scala.sys

import zio.{Runtime, Scope, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}
import zio.http.ServerConfig
import zio.logging.{LogFormat, file}

import Serializer.given

object Server extends ZIOAppDefault:
  override val bootstrap: ZLayer[ZIOAppArgs, Any, Environment] =
    val serverDir =  s"${sys.props("user.home")}/.poolbalance.z"
    var serverPath = Paths.get(serverDir)
    if !Files.exists(serverPath) then serverPath = Files.createDirectory(serverPath)
    Runtime.removeDefaultLoggers >>> file( Path.of(s"$serverDir/server.log") )

  override def run: ZIO[Environment & (ZIOAppArgs & Scope ), Any, Any] =
    for
      conf   <- Resources.loadZIOConfig("server.conf")
      host   =  conf.getString("host")
      port   =  conf.getInt("port")
      config =  ServerConfig.default.binding(host, port)
      _      <- ZIO.log(s"*** Server running at http://$host:$port")
      server <- zio.http.Server
                  .serve(Router.router)
                  .provide(
                    ServerConfig.live(config),
                    zio.http.Server.live,
                    Store.dataSourceLayer,
                    Store.namingStrategyLayer,
                    Store.licenseCacheLayer,
                    Store.layer,
                    Handler.layer
                  )
                  .exitCode
    yield server