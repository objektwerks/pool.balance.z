package objektwerks

import zio.{UIO, ZIO, ZLayer}
import zio.json.{DecoderOps, EncoderOps}

import Serializer.given

final case class Handler(authorizer: Authorizer,
                         validator: Validator,
                         store: Store):
  def handle(command: Command): UIO[Event] =
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
  val layer: ZLayer[Authorizer & Validator & Store, Nothing, Handler] =
    ZLayer {
      for
        authorizer <- ZIO.service[Authorizer]
        validator  <- ZIO.service[Validator]
        store      <- ZIO.service[Store]
      yield Handler(authorizer, validator, store)
    }