package objektwerks

import com.typesafe.config.Config

import io.getquill.*
import io.getquill.jdbczio.Quill
import io.getquill.jdbczio.Quill.Postgres

import java.io.IOException
import java.sql.SQLException

import zio.{ZIO, ZLayer}

case class Store(quill: Quill.Postgres[SnakeCase]):
  import quill.*

  inline def addPool(pool: Pool): ZIO[Any, SQLException, Long] =
    run( query[Pool].insertValue( lift(pool) ).returningGenerated(_.id) )

  inline def updatePool(pool: Pool): ZIO[Any, SQLException, Long] =
    run( query[Pool].filter(_.id == lift(pool.id) ).updateValue( lift(pool) ) )

  inline def listPools: ZIO[Any, SQLException, List[Pool]] = run( query[Pool] )

  inline def addCleaning(cleaning: Cleaning): ZIO[Any, SQLException, Long] =
    run( query[Cleaning].insertValue( lift(cleaning) ).returningGenerated(_.id) )

  inline def listCleanings: ZIO[Any, SQLException, List[Cleaning]] = run( query[Cleaning] )

  inline def addMeasurement(measurement: Measurement): ZIO[Any, SQLException, Long] =
    run( query[Measurement].insertValue( lift(measurement) ).returningGenerated(_.id) )

  inline def listMeasurements: ZIO[Any, SQLException, List[Measurement]] = run( query[Measurement] )

  inline def addChemical(chemical: Chemical): ZIO[Any, SQLException, Long] =
    run( query[Chemical].insertValue( lift(chemical) ).returningGenerated(_.id) )

  inline def listChemicals: ZIO[Any, SQLException, List[Chemical]] = run( query[Chemical] )

object Store:
  val layer: ZLayer[Postgres[SnakeCase], Nothing, Store] = ZLayer.fromFunction(apply(_))