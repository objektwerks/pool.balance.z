package objektwerks

import zio.{Task, ZIO, ZLayer}
import zio.json.{DecoderOps, EncoderOps}

import Serializer.given

final case class Handler(authorizer: Authorizer,
                         validator: Validator,
                         store: Store):
  def handle(command: Command): Task[Event] =
    command match  // TODO! Handler > Authorizer > Validator > Handler > Store
      case c @ ListPools()                  => listPools(c)
      case c @ SavePool(pool)               => savePool(c)
      case c @ ListCleanings()              => listCleanings(c)
      case c @ SaveCleaning(cleaning)       => saveCleaning(c)
      case c @ ListMeasurements()           => listMeasurements(c)
      case c @ SaveMeasurement(measurement) => saveMeasurement(c)
      case c @ ListChemicals()              => listChemicals(c)
      case c @ SaveChemical(chemical)       => saveChemical(c)

  def listPools(command: ListPools): Task[Event] =
    for
      pools <- store.listPools
    yield PoolsListed(pools)

  def savePool(command: SavePool): Task[Event] = PoolSaved(0L)

  def listCleanings(command: ListCleanings): Task[Event] = CleaningsListed(Nil)

  def saveCleaning(command: SaveCleaning): Task[Event] = CleaningSaved(0L)

  def listMeasurements(command: ListMeasurements): Task[Event] = MeasurementsListed(Nil)

  def saveMeasurement(command: SaveMeasurement): Task[Event] = MeasurementSaved(0L)

  def listChemicals(command: ListChemicals): Task[Event] = ChemicalsListed(Nil)

  def saveChemical(command: SaveChemical): Task[Event] = ChemicalSaved(0L)

object Handler:
  val layer: ZLayer[Authorizer & Validator & Store, Nothing, Handler] =
    ZLayer {
      for
        authorizer <- ZIO.service[Authorizer]
        validator  <- ZIO.service[Validator]
        store      <- ZIO.service[Store]
      yield Handler(authorizer, validator, store)
    }