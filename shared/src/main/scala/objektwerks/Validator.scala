package objektwerks

import zio.{ZIO, ZLayer}

final case class Validator():  // TODO!
  def validate(entity: Entity) = ZIO.succeed(true)

object Validator:
  val layer: ZLayer[Any, Nothing, Validator] = ZLayer.succeed(apply())