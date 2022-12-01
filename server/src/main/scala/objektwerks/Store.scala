package objektwerks

import io.getquill.*
import io.getquill.jdbczio.Quill
import io.getquill.jdbczio.Quill.Postgres

import zio.{Task, ZLayer}

final case class Store(quill: Quill.Postgres[SnakeCase]):
  import quill.*

  inline def addAccount(account: Account): Task[Long] =
    run( query[Account].insertValue( lift(account) ).returningGenerated(_.id) )

  inline def updateAcount(account: Account): Task[Long] =
    run( query[Account].filter(_.id == lift(account.id) ).updateValue( lift(account) ) )

  inline def listAccounts: Task[List[Account]] = run( query[Account] )

  inline def addPool(pool: Pool): Task[Long] =
    run( query[Pool].insertValue( lift(pool) ).returningGenerated(_.id) )

  inline def updatePool(pool: Pool): Task[Long] =
    run( query[Pool].filter(_.id == lift(pool.id) ).updateValue( lift(pool) ) )

  inline def listPools: Task[List[Pool]] = run( query[Pool] )

  inline def addCleaning(cleaning: Cleaning): Task[Long] =
    run( query[Cleaning].insertValue( lift(cleaning) ).returningGenerated(_.id) )

  inline def updateCleaning(cleaning: Cleaning): Task[Long] =
    run( query[Cleaning].filter(_.id == lift(cleaning.id) ).updateValue( lift(cleaning) ) )

  inline def listCleanings: Task[List[Cleaning]] = run( query[Cleaning] )

  inline def addMeasurement(measurement: Measurement): Task[Long] =
    run( query[Measurement].insertValue( lift(measurement) ).returningGenerated(_.id) )

  inline def updateMeasurement(measurement: Measurement): Task[Long] =
    run( query[Measurement].filter(_.id == lift(measurement.id) ).updateValue( lift(measurement) ) )

  inline def listMeasurements: Task[List[Measurement]] = run( query[Measurement] )

  inline def addChemical(chemical: Chemical): Task[Long] =
    run( query[Chemical].insertValue( lift(chemical) ).returningGenerated(_.id) )

  inline def updateChemical(chemical: Chemical): Task[Long] =
    run( query[Chemical].filter(_.id == lift(chemical.id) ).updateValue( lift(chemical) ) )

  inline def listChemicals: Task[List[Chemical]] = run( query[Chemical] )

  inline def addFault(fault: Fault): Task[Long] =
    run( query[Fault].insertValue( lift(fault) ) )

  inline def listFaults: Task[List[Fault]] = run( query[Fault] )

object Store:
  val layer: ZLayer[Postgres[SnakeCase], Nothing, Store] = ZLayer.fromFunction(apply(_))