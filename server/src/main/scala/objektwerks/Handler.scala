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
        case c @ ListPools()                  => listPools(c)
        case c @ SavePool(pool)               => savePool(c)
        case c @ ListCleanings()              => CleaningsListed(Nil)
        case c @ SaveCleaning(cleaning)       => CleaningSaved(0L)
        case c @ ListMeasurements()           => MeasurementsListed(Nil)
        case c @ SaveMeasurement(measurement) => MeasurementSaved(0L)
        case c @ ListChemicals()              => ChemicalsListed(Nil)
        case c @ SaveChemical(chemical)       => ChemicalSaved(0L)
    )

  def listPools(command: ListPools): PoolsListed = PoolsListed(Nil)

  def savePool(command: SavePool): PoolSaved = PoolSaved(0L)

object Handler:
  val layer: ZLayer[Authorizer & Validator & Store, Nothing, Handler] =
    ZLayer {
      for
        authorizer <- ZIO.service[Authorizer]
        validator  <- ZIO.service[Validator]
        store      <- ZIO.service[Store]
      yield Handler(authorizer, validator, store)
    }