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
        case c @ ListCleanings()              => listCleanings(c)
        case c @ SaveCleaning(cleaning)       => saveCleaning(c)
        case c @ ListMeasurements()           => listMeasurements(charArrayOps())
        case c @ SaveMeasurement(measurement) => saveMeasurement(c)
        case c @ ListChemicals()              => ChemicalsListed(Nil)
        case c @ SaveChemical(chemical)       => ChemicalSaved(0L)
    )

  def listPools(command: ListPools): PoolsListed = PoolsListed(Nil)

  def savePool(command: SavePool): PoolSaved = PoolSaved(0L)

  def listCleanings(command: ListChemicals): CleaningsListed = CleaningsListed(Nil)

  def saveCleaning(command: SaveCleaning): CleaningSaved = CleaningSaved(0L)

  def listMeasurements(command: ListMeasurements): MeasurementsListed = MeasurementsListed(Nil)

  def saveMeasurement(command: SaveMeasurement): MeasurementSaved = MeasurementSaved(0L)

object Handler:
  val layer: ZLayer[Authorizer & Validator & Store, Nothing, Handler] =
    ZLayer {
      for
        authorizer <- ZIO.service[Authorizer]
        validator  <- ZIO.service[Validator]
        store      <- ZIO.service[Store]
      yield Handler(authorizer, validator, store)
    }