package objektwerks

import zio.{Task, ZIO, ZLayer}
import zio.json.{DecoderOps, EncoderOps}

import Serializer.given

final case class Handler(store: Store):
  def handle[E <: Event](command: Command): Task[Event] =
    command match
      case Register(emailAddress)                => register(emailAddress)
      case Login(emailAddress, pin)              => login(emailAddress, pin)
      case Deactivate(license)                   => deactivate(license)
      case Reactivate(license)                   => reactivate(license)
      case ListPools(license)                    => listPools
      case SavePool(license, pool)               => savePool(pool)
      case ListCleanings(license)                => listCleanings
      case SaveCleaning(license, cleaning)       => saveCleaning(cleaning)
      case ListMeasurements(license)             => listMeasurements
      case SaveMeasurement(license, measurement) => saveMeasurement(measurement)
      case ListChemicals(license)                => listChemicals
      case SaveChemical(license, chemical)       => saveChemical(chemical)

  def register(emailAddress: String): Task[Registered] = ???

  def login(emailAddress: String, pin: String): Task[LoggedIn] = ???

  def deactivate(license: String): Task[Deactivated] = ???

  def reactivate(license: String): Task[Reactivated] = ???

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
  val layer: ZLayer[Store, Nothing, Handler] =
    ZLayer {
      for
        store      <- ZIO.service[Store]
      yield Handler(store)
    }