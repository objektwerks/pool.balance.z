package objektwerks

import com.typesafe.scalalogging.LazyLogging

import java.awt.EventQueue
import java.text.NumberFormat

import scalafx.collections.ObservableBuffer
import scalafx.beans.property.ObjectProperty

import Entity.given
import Measurement.*

object Model extends LazyLogging:
  val observablePools = ObservableBuffer[Pool]()
  val observableCleanings = ObservableBuffer[Cleaning]()
  val observableMeasurements = ObservableBuffer[Measurement]()
  val observableChemicals = ObservableBuffer[Chemical]()

  val selectedPoolId = ObjectProperty[Long](0)
  val selectedCleaningId = ObjectProperty[Long](0)
  val selectedMeasurementId = ObjectProperty[Long](0)
  val selectedChemicalId = ObjectProperty[Long](0)

  selectedPoolId.onChange { (_, oldPoolId, newPoolId) =>
    logger.info(s"*** Model: selected pool id onchange event: $oldPoolId -> $newPoolId")
    cleanings(newPoolId)
    measurements(newPoolId)
    chemicals(newPoolId)
  }

  observableMeasurements.onChange { (_, _) =>
    logger.info(s"*** Model: observable measurements onchange event.")
    EventQueue.invokeLater( () => dashboard() )
  }

  val currentTotalChlorine = ObjectProperty[Int](0)
  val averageTotalChlorine = ObjectProperty[Int](0)
  def isTotalChlorineInRange(value: Int): Boolean = totalChlorineRange.contains(value)

  val currentFreeChlorine = ObjectProperty[Int](0)
  val averageFreeChlorine = ObjectProperty[Int](0)
  def isFreeChlorineInRange(value: Int): Boolean = freeChlorineRange.contains(value)

  val currentCombinedChlorine = ObjectProperty[Double](0)
  val averageCombinedChlorine = ObjectProperty[Double](0)
  def isCombinedChlorineInRange(value: Double): Boolean = combinedChlorineRange.contains(value)

  val currentPh = ObjectProperty[Double](0)
  val averagePh = ObjectProperty[Double](0)
  def isPhInRange(value: Double): Boolean = phRange.contains(value)

  val currentCalciumHardness = ObjectProperty[Int](0)
  val averageCalciumHardness = ObjectProperty[Int](0)
  def isCalciumHardnessInRange(value: Int): Boolean = calciumHardnessRange.contains(value)

  val currentTotalAlkalinity = ObjectProperty[Int](0)
  val averageTotalAlkalinity = ObjectProperty[Int](0)
  def isTotalAlkalinityInRange(value: Int): Boolean = totalAlkalinityRange.contains(value)

  val currentCyanuricAcid = ObjectProperty[Int](0)
  val averageCyanuricAcid = ObjectProperty[Int](0)
  def isCyanuricAcidInRange(value: Int): Boolean = cyanuricAcidRange.contains(value)

  val currentTotalBromine = ObjectProperty[Int](0)
  val averageTotalBromine = ObjectProperty[Int](0)
  def isTotalBromineInRange(value: Int): Boolean = totalBromineRange.contains(value)

  val currentSalt = ObjectProperty[Int](0)
  val averageSalt = ObjectProperty[Int](0)
  def isSaltInRange(value: Int): Boolean = saltRange.contains(value)

  val currentTemperature = ObjectProperty[Int](0)
  val averageTemperature = ObjectProperty[Int](0)
  def isTemperatureInRange(value: Int): Boolean = temperatureRange.contains(value)

  def currentPool(): Option[Pool] = observablePools.find( pool => pool.id == selectedPoolId.get )

  def currentCleaning(): Option[Cleaning] = observableCleanings.find( cleaning => cleaning.id == selectedCleaningId.get )

  def currentMeasurement(): Option[Measurement] = observableMeasurements.find( measurement => measurement.id == selectedMeasurementId.get )

  def currentChemical(): Option[Chemical] = observableChemicals.find( chemical => chemical.id == selectedChemicalId.get )

  def pools(): Unit =
    observablePools.clear()
    // todo observablePools ++= store.pools()
    ()

  def add(pool: Pool): Pool =
    val newPool = ??? // todo store.add(pool)
    observablePools += newPool
    observablePools.sort()
    // todo selectedPoolId.value = newPool.id
    newPool

  def update(pool: Pool): Unit =
    // todo store.update(pool)
    observablePools.update(1, pool) // bug!!!
    observablePools.sort()
    selectedPoolId.value = pool.id

  def cleanings(poolId: Long): Unit =
    observableCleanings.clear()
    // todo observableCleanings ++= store.cleanings(poolId)

  def add(cleaning: Cleaning): Cleaning =
    val newCleaning = ??? // todo store.add(cleaning)
    observableCleanings += newCleaning
    observableCleanings.sort()
    // todo selectedCleaningId.value = newCleaning.id
    newCleaning

  def update(cleaning: Cleaning): Unit =
    // todo store.update(cleaning)
    observableCleanings.update(1, cleaning) // bug!!!
    observableCleanings.sort()
    selectedCleaningId.value = cleaning.id

  def measurements(poolId: Long): Unit =
    observableMeasurements.clear()
    // todo observableMeasurements ++= store.measurements(poolId) 

  def add(measurement: Measurement): Measurement =
    val newMeasurement = ??? // todo store.add(measurement)
    observableMeasurements += newMeasurement
    observableMeasurements.sort()
    // todo selectedMeasurementId.value = newMeasurement.id
    newMeasurement

  def update(measurement: Measurement): Unit =
    // todo store.update(measurement)
    observableMeasurements.update(1, measurement) // bug!!!
    observableMeasurements.sort()
    selectedMeasurementId.value = measurement.id

  def chemicals(poolId: Long): Unit =
    observableChemicals.clear()
    // todo observableChemicals ++= store.chemicals(poolId) 
  
  def add(chemical: Chemical): Chemical =
    val newChemical = ??? // todo  store.add(chemical)
    observableChemicals += newChemical
    observableChemicals.sort()
    // todo selectedChemicalId.value = newChemical.id      
    newChemical

  def update(chemical: Chemical): Unit =
    // todo store.update(chemical)
    observableChemicals.update(1, chemical) // bug!!!
    observableChemicals.sort()
    selectedChemicalId.value = chemical.id

  private def dashboard(): Unit =
    val numberFormat = NumberFormat.getNumberInstance()
    numberFormat.setMaximumFractionDigits(1)
    observableMeasurements.headOption.foreach { measurement =>
      onCurrent(measurement, numberFormat)
      onAverage(numberFormat)
    }

  private def onCurrent(measurement: Measurement, numberFormat: NumberFormat): Unit =
    currentTotalChlorine.value = measurement.totalChlorine
    currentFreeChlorine.value = measurement.freeChlorine
    currentCombinedChlorine.value = numberFormat.format( measurement.combinedChlorine ).toDouble
    currentPh.value = numberFormat.format( measurement.ph ).toDouble
    currentCalciumHardness.value = measurement.calciumHardness
    currentTotalAlkalinity.value = measurement.totalAlkalinity
    currentCyanuricAcid.value = measurement.cyanuricAcid
    currentTotalBromine.value = measurement.totalBromine
    currentSalt.value = measurement.salt
    currentTemperature.value = measurement.temperature

  private def onAverage(numberFormat: NumberFormat): Unit =
    val count = observableMeasurements.length
    averageTotalChlorine.value = observableMeasurements.map(_.totalChlorine).sum / count
    averageFreeChlorine.value = observableMeasurements.map(_.freeChlorine).sum / count
    averageCombinedChlorine.value = numberFormat.format( observableMeasurements.map(_.combinedChlorine).sum / count ).toDouble
    averagePh.value = numberFormat.format( observableMeasurements.map(_.ph).sum / count ).toDouble
    averageCalciumHardness.value = observableMeasurements.map(_.calciumHardness).sum / count
    averageTotalAlkalinity.value = observableMeasurements.map(_.totalAlkalinity).sum / count
    averageCyanuricAcid.value = observableMeasurements.map(_.cyanuricAcid).sum / count
    averageTotalBromine.value = observableMeasurements.map(_.totalBromine).sum / count
    averageSalt.value = observableMeasurements.map(_.salt).sum / count
    averageTemperature.value = observableMeasurements.map(_.temperature).sum / count