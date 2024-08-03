package objektwerks

import com.typesafe.config.Config

import java.time.LocalDate
import java.util.concurrent.TimeUnit
import javax.sql.DataSource

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

  def login(emailAddress: String, pin: String): Task[Option[Account]] =
    run(
      query[Account]
        .filter(_.emailAddress == lift(emailAddress))
        .filter(_.pin == lift(pin))
    ).map(result => result.headOption)

  def addAccount(account: Account): Task[Long] =
    transaction(
      run(query[Account].insertValue(lift(account)).returningGenerated(_.id))
    )

  def deactivateAccount(license: String): Task[Account] =
    transaction(
      run( 
        query[Account]
          .filter( _.license == lift(license) )
          .update( _.deactivated -> lift(LocalDate.now.toEpochDay), _.activated -> lift(0) )
          .returning(account => account)
      )
    )

  def reactivateAccount(license: String): Task[Account] =
    transaction(
      run(
        query[Account]
          .filter( _.license == lift(license) )
          .update( _.activated -> lift(LocalDate.now.toEpochDay), _.deactivated -> lift(0) )
          .returning(account => account)
      )
    )

  def listPools(license: String): Task[List[Pool]] = run( query[Pool].filter(_.license == lift(license)).sortBy(_.name)(Ord.asc) )

  def addPool(pool: Pool): Task[Long] =
    transaction (
      run( query[Pool].insertValue( lift(pool) ).returningGenerated(_.id) )
    )

  def updatePool(pool: Pool): Task[Long] =
    transaction (
      run( query[Pool].filter( _.id == lift(pool.id) ).updateValue( lift(pool) ) )
    )

  def listCleanings(poolId: Long): Task[List[Cleaning]] =
    run( query[Cleaning].filter( _.poolId == lift(poolId) ).sortBy(_.cleaned)(Ord.desc) )

  def addCleaning(cleaning: Cleaning): Task[Long] =
    transaction (
      run( query[Cleaning].insertValue( lift(cleaning) ).returningGenerated(_.id) )
    )

  def updateCleaning(cleaning: Cleaning): Task[Long] =
    transaction (
      run( query[Cleaning].filter( _.id == lift(cleaning.id) ).updateValue( lift(cleaning) ) )
    )

  def listMeasurements(poolId: Long): Task[List[Measurement]] =
    run( query[Measurement].filter( _.poolId == lift(poolId) ).sortBy(_.measured)(Ord.desc) )

  def addMeasurement(measurement: Measurement): Task[Long] =
    transaction (
      run( query[Measurement].insertValue( lift(measurement) ).returningGenerated(_.id) )
    )

  def updateMeasurement(measurement: Measurement): Task[Long] =
    transaction (
      run( query[Measurement].filter( _.id == lift(measurement.id) ).updateValue( lift(measurement) ) )
    )

  def listChemicals(poolId: Long): Task[List[Chemical]] =
    run( query[Chemical].filter( _.poolId == lift(poolId) ).sortBy(_.added)(Ord.desc) )

  def addChemical(chemical: Chemical): Task[Long] =
    transaction (
      run( query[Chemical].insertValue( lift(chemical) ).returningGenerated(_.id) )
    )

  def updateChemical(chemical: Chemical): Task[Long] =
    transaction (
      run( query[Chemical].filter( _.id == lift(chemical.id) ).updateValue( lift(chemical) ) )
    )

object Store:
  def dataSourceLayer(config: Config): ZLayer[Any, Throwable, DataSource] =
    Quill.DataSource.fromConfig(config)

  val namingStrategyLayer: ZLayer[DataSource, Nothing, Postgres[SnakeCase]] =
    Quill.Postgres.fromNamingStrategy(SnakeCase)

  val licenseCacheLayer: ZLayer[Any, Nothing, Cache[String, Nothing, String]] =
    ZLayer.fromZIO {
      Cache.make(capacity = 100,
                 timeToLive = Duration(12, TimeUnit.HOURS),
                 lookup = Lookup( (license: String) => ZIO.succeed( if license.isLicense then license else "" ) )
                )
    }

  val layer: ZLayer[Postgres[SnakeCase] & Cache[String, Nothing, String], Nothing, Store] =
    ZLayer.fromFunction(apply(_, _))