package objektwerks

import com.typesafe.config.Config

import io.getquill.*

class Store(config: Config):
  val ctx = new PostgresJdbcContext(SnakeCase, config)
  import ctx.*

  inline def addPool(pool: Pool): Long =
    run( query[Pool].insertValue( lift(pool) ).returningGenerated(_.id) )

  inline def updatePool(pool: Pool): Long =
    run( query[Pool].filter(_.id == lift(pool.id) ).updateValue( lift(pool) ) )

  inline def listPools: List[Pool] = run( query[Pool] )

  inline def addCleaning(cleaning: Cleaning): Long =
    run( query[Cleaning].insertValue( lift(cleaning) ).returningGenerated(_.id) )

  inline def updateCleaning(cleaning: Cleaning): Long =
    run( query[Cleaning].filter(_.id == lift(cleaning.id) ).updateValue( lift(cleaning) ) )

  inline def listCleanings: List[Cleaning] = run( query[Cleaning] )

  inline def addMeasurement(measurement: Measurement): Long =
    run( query[Measurement].insertValue( lift(measurement) ).returningGenerated(_.id) )

  inline def updateMeasurement(measurement: Measurement): Long =
    run( query[Measurement].filter(_.id == lift(measurement.id) ).updateValue( lift(measurement) ) )

  inline def listMeasurements: List[Measurement] = run( query[Measurement] )

  inline def addChemical(chemical: Chemical): Long =
    run( query[Chemical].insertValue( lift(chemical) ).returningGenerated(_.id) )

  inline def updateChemical(chemical: Chemical): Long =
    run( query[Chemical].filter(_.id == lift(chemical.id) ).updateValue( lift(chemical) ) )

  inline def listChemicals: List[Chemical] = run( query[Chemical] )