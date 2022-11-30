package objektwerks

import zio.{ZIO, ZLayer}

final case class Authorizer(): // TODO!
  def authorize(license: String): ZIO[Any, Nothing, Boolean] = ZIO.succeed(true)

object Authorizer:
  val layer: ZLayer[Any, Nothing, Authorizer] = ZLayer.succeed(apply())