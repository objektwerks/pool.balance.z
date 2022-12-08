package objektwerks

import com.typesafe.config.Config

import javax.sql.DataSource
import java.util.concurrent.TimeUnit

import io.getquill.*
import io.getquill.jdbczio.Quill
import io.getquill.jdbczio.Quill.Postgres

import zio.{Duration, Task, UIO, ZIO, ZLayer}
import zio.cache.{Cache, Lookup}

import Validator.*

final case class Store(quill: Quill.Postgres[SnakeCase],
                       cache: Cache[String, Nothing, String]):
  import quill.*

  private def isCached(license: String): UIO[Boolean] = cache.contains(license)

  private def cache(license: String): UIO[String] = cache.get(license)

  private def isStored(license: String): Task[Boolean] =
    run( query[Account].filter( _.license == lift(license) ).nonEmpty )

  def authorize(license: String): Task[Boolean] =
    ZIO.ifZIO( isCached(license) )(
      onTrue = ZIO.succeed(true),
      onFalse = ZIO.ifZIO( isStored(license) )(
        onTrue = cache(license).map(l => l.isLicense),
        onFalse = ZIO.succeed(false)
      )
    )

  def register(account: Account): Task[Long] = addAccount(account)

  def login(pin: String): Task[Option[Account]] =
    run( query[Account].filter( _.pin == lift(pin) ) ).map(result => result.headOption)

  def addAccount(account: Account): Task[Long] =
    transaction(
      run(query[Account].insertValue(lift(account)).returningGenerated(_.id))
    )

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

object Store:
  val dataSourceLayer: ZLayer[Any, Throwable, DataSource] =
    Quill.DataSource.fromConfig( Resources.load(path = "server.conf", section = "db") )

  val namingStrategyLayer: ZLayer[DataSource, Nothing, Postgres[SnakeCase]] = Quill.Postgres.fromNamingStrategy(SnakeCase)

  val licenseCacheLayer: ZLayer[Any, Nothing, Cache[String, Nothing, String]] =
    ZLayer.fromZIO {
      Cache.make(capacity = 100,
                timeToLive = Duration(12, TimeUnit.HOURS),
                lookup = Lookup( (license: String) =>
                  ZIO.log(s"*** License cache lookup: $license") zipRight
                  ZIO.succeed( if license.isLicense then license else "" ) )
                )
    }

  val layer: ZLayer[Postgres[SnakeCase] & Cache[String, Nothing, String], Nothing, Store] = ZLayer.fromFunction(apply(_, _))