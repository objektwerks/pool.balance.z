package objektwerks

import zio.{Task, ZIO, ZLayer}
import zio.json.{DecoderOps, EncoderOps}

import Serializer.given
import scala.tools.nsc.transform.patmat.Logic.PropositionalLogic.AnalysisBudget.formulaSizeExceeded

final case class Handler(authorizer: Authorizer,
                         validator: Validator,
                         store: Store):
  def handle(command: Command): Task[Event] =
    command match  // TODO! Handler > Authorizer > Validator > Handler > Store
      case ListPools()                  => listPools
      case SavePool(pool)               => savePool(pool)
      case ListCleanings()              => listCleanings
      case SaveCleaning(cleaning)       => saveCleaning(cleaning)
      case ListMeasurements()           => listMeasurements
      case SaveMeasurement(measurement) => saveMeasurement(measurement)
      case ListChemicals()              => listChemicals
      case SaveChemical(chemical)       => saveChemical(chemical)

  def listPools: Task[Event] =
    for
      pools <- store.listPools
    yield PoolsListed(pools)

  def savePool(pool: Pool): Task[Event] =
    for
      id <- if pool.id == 0 then store.addPool(pool) else store.updatePool(pool)
    yield PoolSaved(id)

  def listCleanings: Task[Event] =
    for
      cleanings <- store.listCleanings
    yield CleaningsListed(cleanings)

  def saveCleaning(cleaning: Cleaning): Task[Event] =
    for
      id <- if cleaning.id == 0 then store.addCleaning(cleaning) else store.updateCleaning(cleaning)
    yield CleaningSaved(id)

  def listMeasurements: Task[Event] =
    for
      measurements <- store.listMeasurements
    yield MeasurementsListed(measurements)

  def saveMeasurement(measurement: Measurement): Task[Event] =
    for
      id <- if measurement.id == 0 then store.addMeasurement(measurement) else store.updateMeasurement(measurement)
    yield MeasurementSaved(id)

  def listChemicals: Task[Event] =
    for
      chemicals <- store.listChemicals
    yield ChemicalsListed(chemicals)

  def saveChemical(chemical: Chemical): Task[Event] = ChemicalSaved(0L)

object Handler:
  val layer: ZLayer[Authorizer & Validator & Store, Nothing, Handler] =
    ZLayer {
      for
        authorizer <- ZIO.service[Authorizer]
        validator  <- ZIO.service[Validator]
        store      <- ZIO.service[Store]
      yield Handler(authorizer, validator, store)
    }