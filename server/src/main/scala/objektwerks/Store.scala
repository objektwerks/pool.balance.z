package objektwerks

import com.typesafe.config.Config

import javax.sql.DataSource

import io.getquill.*
import io.getquill.jdbczio.Quill
import io.getquill.jdbczio.Quill.Postgres

import zio.{Task, ZLayer}

final case class Store(quill: Quill.Postgres[SnakeCase]):
  import quill.*

  def authorize(license: String): Task[Boolean] =
    run( query[Account].filter(_.license == lift(license)).nonEmpty )

  def register(account: Account): Task[Long] = addAccount(account)

  def listAccounts: Task[List[Account]] = run( query[Account] )

  def addAccount(account: Account): Task[Long] =
    run( query[Account].insertValue( lift(account) ).returningGenerated(_.id) )

  def updateAcount(account: Account): Task[Long] =
    run( query[Account].filter(_.id == lift(account.id) ).updateValue( lift(account) ) )

  def listPools: Task[List[Pool]] = run( query[Pool] )

  def addPool(pool: Pool): Task[Long] =
    run( query[Pool].insertValue( lift(pool) ).returningGenerated(_.id) )

  def updatePool(pool: Pool): Task[Long] =
    run( query[Pool].filter(_.id == lift(pool.id) ).updateValue( lift(pool) ) )

  def listCleanings: Task[List[Cleaning]] = run( query[Cleaning] )

  def addCleaning(cleaning: Cleaning): Task[Long] =
    run( query[Cleaning].insertValue( lift(cleaning) ).returningGenerated(_.id) )

  def updateCleaning(cleaning: Cleaning): Task[Long] =
    run( query[Cleaning].filter(_.id == lift(cleaning.id) ).updateValue( lift(cleaning) ) )

  def listMeasurements: Task[List[Measurement]] = run( query[Measurement] )

  def addMeasurement(measurement: Measurement): Task[Long] =
    run( query[Measurement].insertValue( lift(measurement) ).returningGenerated(_.id) )

  def updateMeasurement(measurement: Measurement): Task[Long] =
    run( query[Measurement].filter(_.id == lift(measurement.id) ).updateValue( lift(measurement) ) )

  def listChemicals: Task[List[Chemical]] = run( query[Chemical] )

  def addChemical(chemical: Chemical): Task[Long] =
    run( query[Chemical].insertValue( lift(chemical) ).returningGenerated(_.id) )

  def updateChemical(chemical: Chemical): Task[Long] =
    run( query[Chemical].filter(_.id == lift(chemical.id) ).updateValue( lift(chemical) ) )

  def listEmails: Task[List[Email]] = run( query[Email] )

  def addEmail(email: Email): Task[Long] =
    run( query[Email].insertValue( lift(email) ).returningGenerated(_.id) )

  def listFaults: Task[List[Fault]] = run( query[Fault] )

  def addFault(fault: Fault): Task[Long] =
    run( query[Fault].insertValue( lift(fault) ) )

object Store:
  def namingStrategy: ZLayer[DataSource, Nothing, Postgres[SnakeCase]] = Quill.Postgres.fromNamingStrategy(SnakeCase)

  def datasource(config: Config): ZLayer[Any, Throwable, DataSource] = Quill.DataSource.fromConfig(config)

  def layer: ZLayer[Postgres[SnakeCase], Nothing, Store] = ZLayer.fromFunction(apply(_))