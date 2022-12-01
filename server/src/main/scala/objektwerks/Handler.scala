package objektwerks

import zio.{Task, ZIO, ZLayer}
import zio.json.{DecoderOps, EncoderOps}

import Serializer.given

final case class Handler(authorizer: Authorizer,
                         validator: Validator,
                         store: Store):
  def handle[E <: Event](command: Command): Task[Event] =
    command match  // TODO! Handler > Authorizer > Validator > Handler > Store
      case Register(emailAddress)       => register(emailAddress)
      case Login(emailAddress, pin)     => login(emailAddress, pin)
      case ListPools()                  => listPools
      case SavePool(pool)               => savePool(pool)
      case ListCleanings()              => listCleanings
      case SaveCleaning(cleaning)       => saveCleaning(cleaning)
      case ListMeasurements()           => listMeasurements
      case SaveMeasurement(measurement) => saveMeasurement(measurement)
      case ListChemicals()              => listChemicals
      case SaveChemical(chemical)       => saveChemical(chemical)

  def register(emailAddress: String): Task[Registered] = ???

  def login(emailAddress: String, pin: String): Task[LoggedIn] = ???

  def listPools: Task[PoolsListed] =
    for
      pools <- store.listPools
    yield PoolsListed(pools)

  def savePool(pool: Pool): Task[PoolSaved] =
    for
      id <- if pool.id == 0 then store.addPool(pool) else store.updatePool(pool)
    yield PoolSaved(id)

  def listCleanings: Task[CleaningsListed] =
    for
      cleanings <- store.listCleanings
    yield CleaningsListed(cleanings)

  def saveCleaning(cleaning: Cleaning): Task[CleaningSaved] =
    for
      id <- if cleaning.id == 0 then store.addCleaning(cleaning) else store.updateCleaning(cleaning)
    yield CleaningSaved(id)

  def listMeasurements: Task[MeasurementsListed] =
    for
      measurements <- store.listMeasurements
    yield MeasurementsListed(measurements)

  def saveMeasurement(measurement: Measurement): Task[MeasurementSaved] =
    for
      id <- if measurement.id == 0 then store.addMeasurement(measurement) else store.updateMeasurement(measurement)
    yield MeasurementSaved(id)

  def listChemicals: Task[ChemicalsListed] =
    for
      chemicals <- store.listChemicals
    yield ChemicalsListed(chemicals)

  def saveChemical(chemical: Chemical): Task[ChemicalSaved] =
    for
      id <- if chemical.id == 0 then store.addChemical(chemical) else store.updateChemical(chemical)
    yield ChemicalSaved(id)

object Handler:
  val layer: ZLayer[Authorizer & Validator & Store, Nothing, Handler] =
    ZLayer {
      for
        authorizer <- ZIO.service[Authorizer]
        validator  <- ZIO.service[Validator]
        store      <- ZIO.service[Store]
      yield Handler(authorizer, validator, store)
    }