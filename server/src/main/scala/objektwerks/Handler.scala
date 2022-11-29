package objektwerks

import zio.{ZIO, ZLayer}
import zio.json.{DecoderOps, EncoderOps}

import Serializer.given

final case class Handler():
  def handle(request: Either[String, Command]) =
    ZIO.succeed(
      request match
        case Right(command) => command match // TODO!
          case ListPools()                  => PoolsListed(Nil).toJson
          case SavePool(pool)               => PoolSaved(pool).toJson
          case ListCleanings()              => CleaningsListed(Nil).toJson
          case SaveCleaning(cleaning)       => CleaningSaved(cleaning).toJson
          case ListMeasurements()           => MeasurementsListed(Nil).toJson
          case SaveMeasurement(measurement) => MeasurementSaved(measurement).toJson
          case ListChemicals()              => ChemicalsListed(Nil).toJson
          case SaveChemical(chemical)       => ChemicalSaved(chemical).toJson
        case Left(cause) => Fault(cause).toJson
    )

object Handler:
  val layer: ZLayer[Any, Nothing, Handler] = ZLayer.succeed(apply())