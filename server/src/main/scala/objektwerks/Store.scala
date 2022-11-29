package objektwerks

import com.typesafe.config.Config

import io.getquill.*

class Store(config: Config):
  val ctx = new PostgresJdbcContext(SnakeCase, config)
  import ctx.*

  def addPool(pool: Pool): Long =
    run( query[Pool].insertValue( lift(pool) ).returningGenerated(_.id) )

  def updatePool(pool: Pool): Long =
    run( query[Pool].filter(_.id == lift(pool.id) ).updateValue( lift(pool) ) )

  def listPools: List[Pool] = run( query[Pool] )

  def addCleaning(cleaning: Cleaning): Long =
    run( query[Cleaning].insertValue( lift(cleaning) ).returningGenerated(_.id) )

  def updateCleaning(cleaning: Cleaning): Long =
    run( query[Cleaning].filter(_.id == lift(cleaning.id) ).updateValue( lift(cleaning) ) )

  def listCleanings: List[Cleaning] = run( query[Cleaning] )

  def addMeasurement(measurement: Measurement): Long =
    run( query[Measurement].insertValue( lift(measurement) ).returningGenerated(_.id) )

  def updateMeasurement(measurement: Measurement): Long =
    run( query[Measurement].filter(_.id == lift(measurement.id) ).updateValue( lift(measurement) ) )

  def listMeasurements: List[Measurement] = run( query[Measurement] )

  def addChemical(chemical: Chemical): Long =
    run( query[Chemical].insertValue( lift(chemical) ).returningGenerated(_.id) )

  def updateChemical(chemical: Chemical): Long =
    run( query[Chemical].filter(_.id == lift(chemical.id) ).updateValue( lift(chemical) ) )

  def listChemicals: List[Chemical] = run( query[Chemical] )