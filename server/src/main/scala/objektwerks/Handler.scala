package objektwerks

import zio.{ZIO, ZLayer}
import zio.json.{DecoderOps, EncoderOps}

import Serializer.given

final case class Handler():
  def handle(command: Command): ZIO[Any, Nothing, Event] =
    ZIO.succeed(
      command match  // TODO! Handler > Authorizer > Validator > Handler > Store
        case ListPools()                  => PoolsListed(Nil)
        case SavePool(pool)               => PoolSaved(0L)
        case ListCleanings()              => CleaningsListed(Nil)
        case SaveCleaning(cleaning)       => CleaningSaved(0L)
        case ListMeasurements()           => MeasurementsListed(Nil)
        case SaveMeasurement(measurement) => MeasurementSaved(0L)
        case ListChemicals()              => ChemicalsListed(Nil)
        case SaveChemical(chemical)       => ChemicalSaved(0L)
    )

object Handler:
  val layer: ZLayer[Any, Nothing, Handler] = ZLayer.succeed(apply())