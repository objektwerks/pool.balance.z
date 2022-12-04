package objektwerks

import com.typesafe.config.Config

import javax.sql.DataSource
import java.util.concurrent.TimeUnit

import io.getquill.*
import io.getquill.jdbczio.Quill
import io.getquill.jdbczio.Quill.Postgres

import zio.{Duration, Task, ZIO, ZLayer}
import zio.cache.{Cache, Lookup}

final case class Store(quill: Quill.Postgres[SnakeCase],
                       cache: Cache[String, Nothing, String]):
  import quill.*

  def authorize(license: String): Task[Boolean] =
    run( query[Account].filter( _.license == lift(license) ).nonEmpty )

  def register(account: Account): Task[Long] = addAccount(account)

  def login(pin: String): Task[Option[Account]] =
    run( query[Account].filter( _.pin == lift(pin) ) ).map(result => result.headOption)

  def deactivateAccount(license: String): Task[Long] =
    transaction(
      run( 
        query[Account]
          .filter( _.license == lift(license) )
          .update( _.deactivated -> lift(Entity.instant), _.activated -> lift("") )
      )
    )

  def reactivateAccount(license: String): Task[Long] =
    transaction(
      run(
        query[Account]
          .filter( _.license == lift(license) )
          .update( _.activated -> lift(Entity.instant), _.deactivated -> lift("") )
      )
    )    

  def listAccounts: Task[List[Account]] = run( query[Account] )

  def addAccount(account: Account): Task[Long] =
    transaction (
      run( query[Account].insertValue( lift(account) ).returningGenerated(_.id) )
    )

  def updateAcount(account: Account): Task[Long] =
    transaction (
      run( query[Account].filter( _.id == lift(account.id) ).updateValue( lift(account) ) )
    )

  def listPools: Task[List[Pool]] = run( query[Pool] )

  def addPool(pool: Pool): Task[Long] =
    transaction (
      run( query[Pool].insertValue( lift(pool) ).returningGenerated(_.id) )
    )

  def updatePool(pool: Pool): Task[Long] =
    transaction (
      run( query[Pool].filter( _.id == lift(pool.id) ).updateValue( lift(pool) ) )
    )

  def listCleanings: Task[List[Cleaning]] = run( query[Cleaning] )

  def addCleaning(cleaning: Cleaning): Task[Long] =
    transaction (
      run( query[Cleaning].insertValue( lift(cleaning) ).returningGenerated(_.id) )
    )

  def updateCleaning(cleaning: Cleaning): Task[Long] =
    transaction (
      run( query[Cleaning].filter( _.id == lift(cleaning.id) ).updateValue( lift(cleaning) ) )
    )

  def listMeasurements: Task[List[Measurement]] = run( query[Measurement] )

  def addMeasurement(measurement: Measurement): Task[Long] =
    transaction (
      run( query[Measurement].insertValue( lift(measurement) ).returningGenerated(_.id) )
    )

  def updateMeasurement(measurement: Measurement): Task[Long] =
    transaction (
      run( query[Measurement].filter( _.id == lift(measurement.id) ).updateValue( lift(measurement) ) )
    )

  def listChemicals: Task[List[Chemical]] = run( query[Chemical] )

  def addChemical(chemical: Chemical): Task[Long] =
    transaction (
      run( query[Chemical].insertValue( lift(chemical) ).returningGenerated(_.id) )
    )

  def updateChemical(chemical: Chemical): Task[Long] =
    transaction (
      run( query[Chemical].filter( _.id == lift(chemical.id) ).updateValue( lift(chemical) ) )
    )

  def listFaults: Task[List[Fault]] = run( query[Fault] )

  def addFault(fault: Fault): Task[Long] =
    transaction (
      run( query[Fault].insertValue( lift(fault) ) )
    )

object Store:
  def dataSource(config: Config): ZLayer[Any, Throwable, DataSource] = Quill.DataSource.fromConfig(config)

  def namingStrategy: ZLayer[DataSource, Nothing, Postgres[SnakeCase]] = Quill.Postgres.fromNamingStrategy(SnakeCase)

  def licenseCache: ZLayer[Any, Nothing, Cache[String, Nothing, String]] = ZLayer.fromZIO{
    Cache.make(capacity = 100,
               timeToLive = Duration(12, TimeUnit.HOURS),
               lookup = Lookup( (license: String) => ZIO.log(s"lookup cached => $license").as(license) ) )
  }

  def layer: ZLayer[Postgres[SnakeCase] & Cache[String, Nothing, String], Nothing, Store] = ZLayer.fromFunction(apply(_, _))
