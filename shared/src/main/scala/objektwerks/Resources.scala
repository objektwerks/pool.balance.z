package objektwerks

import com.typesafe.config.{Config, ConfigFactory}

import java.io.IOException

import zio.ZIO

object Resources:
  def loadConfig(path: String): ZIO[Any, IOException, Config] =
    ZIO.attemptBlockingIO(
      ConfigFactory
        .load(path)
    )

  def loadConfig(path: String, section: String): ZIO[Any, IOException, Config] =
    ZIO.attemptBlockingIO(
      ConfigFactory
        .load(path)
        .getObject(section)
        .toConfig
    )

  def load(path: String, section: String): Config = ConfigFactory.load(path).getConfig(section)