package objektwerks

import com.typesafe.scalalogging.LazyLogging

import java.awt.EventQueue
import java.text.NumberFormat

import scalafx.collections.ObservableBuffer
import scalafx.beans.property.ObjectProperty

import Entity.given
import Measurement.*

object Model extends LazyLogging:
  val selectedPoolId = ObjectProperty[Long](0)
  val selectedCleaningId = ObjectProperty[Long](0)
  val selectedMeasurementId = ObjectProperty[Long](0)
  val selectedChemicalId = ObjectProperty[Long](0)

  selectedPoolId.onChange { (_, oldId, newId) =>
    logger.info(s"*** Model: selected pool id onchange event: $oldId -> $newId")
    cleanings(newId)
    measurements(newId)
    chemicals(newId)
  }

  selectedCleaningId.onChange { (_, oldId, newId) =>
    logger.info(s"*** Model: selected cleaning id onchange event: $oldId -> $newId")
  }

  selectedMeasurementId.onChange { (_, oldId, newId) =>
    logger.info(s"*** Model: selected measurement id onchange event: $oldId -> $newId")
  }

  selectedChemicalId.onChange { (_, oldId, newId) =>
    logger.info(s"*** Model: selected chemical id onchange event: $oldId -> $newId")
  }

  val observableAccount = ObjectProperty[Account](Account.empty)

  val observablePools = ObservableBuffer[Pool]()
  val observableCleanings = ObservableBuffer[Cleaning]()
  val observableMeasurements = ObservableBuffer[Measurement]()
  val observableChemicals = ObservableBuffer[Chemical]()
  val observableFaults = ObservableBuffer[Fault]()

  observablePools.onChange { (_, changes) =>
    logger.info(s"*** Model: observable pools onchange event: $changes")
  }

  observableCleanings.onChange { (_, changes) =>
    logger.info(s"*** Model: observable cleanings onchange event: $changes")
  }

  observableMeasurements.onChange { (_, changes) =>
    logger.info(s"*** Model: observable measurements onchange event: $changes")
    EventQueue.invokeLater( () => dashboard() )
  }

  observableChemicals.onChange { (_, changes) =>
    logger.info(s"*** Model: observable chemicals onchange event: $changes")
  }

  def onFault(source: String, fault: Fault): Unit =
    observableFaults += fault
    logger.error(s"*** $source - $fault")

  def currentPool: Option[Pool] = observablePools.find( pool => pool.id == selectedPoolId.get )

  def currentCleaning: Option[Cleaning] = observableCleanings.find( cleaning => cleaning.id == selectedCleaningId.get )

  def currentMeasurement: Option[Measurement] = observableMeasurements.find( measurement => measurement.id == selectedMeasurementId.get )

  def currentChemical: Option[Chemical] = observableChemicals.find( chemical => chemical.id == selectedChemicalId.get )

  def pools(): Unit =
    Proxy.call(
      ListPools(observableAccount.get.license),
      (event: Event) =>
        event match
          case fault @ Fault(_, _) => onFault("Model.pools", fault)
          case PoolsListed(pools) =>
            observablePools.clear()
            observablePools ++= pools
            observablePools.headOption.collect { pool => selectedPoolId.set(pool.id) }
          case _ => ()
    )

  def add(pool: Pool): Unit =
    Proxy.call(
      SavePool(observableAccount.get.license, pool),
      (event: Event) =>
        event match
          case fault @ Fault(_, _) => onFault("Model.add pool", fault)
          case PoolSaved(id) =>
            observablePools += pool.copy(id = id)
            selectedPoolId.set(pool.id)
          case _ => ()
    )

  def update(pool: Pool): Unit =
    Proxy.call(
      SavePool(observableAccount.get.license, pool),
      (event: Event) =>
        event match
          case fault @ Fault(_, _) => onFault("Model.update pool", fault)
          case PoolSaved(id) =>
            observablePools.update(observablePools.indexOf(pool), pool)
            selectedPoolId.set(pool.id)
          case _ => ()
    )

  def cleanings(poolId: Long): Unit =
    Proxy.call(
      ListCleanings(observableAccount.get.license),
      (event: Event) =>
        event match
          case fault @ Fault(_, _) => onFault("Model.cleanings", fault)
          case CleaningsListed(cleanings) =>
            observableCleanings.clear()
            observableCleanings ++= cleanings
            observableCleanings.headOption.collect { cleaning => selectedCleaningId.set(cleaning.id) }
          case _ => ()
    )

  def add(cleaning: Cleaning): Unit =
    Proxy.call(
      SaveCleaning(observableAccount.get.license, cleaning),
      (event: Event) =>
        event match
          case fault @ Fault(_, _) => onFault("Model.add cleaning", fault)
          case CleaningSaved(id) =>
            observableCleanings += cleaning.copy(id = id)
            selectedCleaningId.set(cleaning.id)
          case _ => ()
    )

  def update(cleaning: Cleaning): Unit =
    Proxy.call(
      SaveCleaning(observableAccount.get.license, cleaning),
      (event: Event) =>
        event match
          case fault @ Fault(_, _) => onFault("Model.update cleaning", fault)
          case CleaningSaved(id) =>
            observableCleanings.update(observableCleanings.indexOf(cleaning), cleaning)
            selectedCleaningId.set(cleaning.id)
          case _ => ()
    )

  def measurements(poolId: Long): Unit =
    Proxy.call(
      ListMeasurements(observableAccount.get.license),
      (event: Event) =>
        event match
          case fault @ Fault(_, _) => onFault("Model.measurements", fault)
          case MeasurementsListed(measurements) =>
            observableMeasurements.clear()
            observableMeasurements ++= measurements
            observableMeasurements.headOption.collect { measurement => selectedMeasurementId.set(measurement.id) }
          case _ => ()
    )

  def add(measurement: Measurement): Unit =
    Proxy.call(
      SaveMeasurement(observableAccount.get.license, measurement),
      (event: Event) =>
        event match
          case fault @ Fault(_, _) => onFault("Model.add measurement", fault)
          case MeasurementSaved(id) =>
            observableMeasurements += measurement.copy(id = id)
            selectedMeasurementId.set(measurement.id)
          case _ => ()
    )

  def update(measurement: Measurement): Unit =
    Proxy.call(
      SaveMeasurement(observableAccount.get.license, measurement),
      (event: Event) =>
        event match
          case fault @ Fault(_, _) => onFault("Model.update measurement", fault)
          case MeasurementSaved(id) =>
            observableMeasurements.update(observableMeasurements.indexOf(measurement), measurement)
            selectedMeasurementId.set(measurement.id)
          case _ => ()
    )

  def chemicals(poolId: Long): Unit =
    Proxy.call(
      ListChemicals(observableAccount.get.license),
      (event: Event) =>
        event match
          case Fault(cause, occurred) => logger.error(s"*** Model.chemicals error: $cause at: $occurred")
          case ChemicalsListed(chemicals) =>
            observableChemicals.clear()
            observableChemicals ++= chemicals
            observableChemicals.headOption.collect { chemical => selectedChemicalId.set(chemical.id) }
          case _ => ()
    )
  
  def add(chemical: Chemical): Unit =
    Proxy.call(
      SaveChemical(observableAccount.get.license, chemical),
      (event: Event) =>
        event match
          case Fault(cause, occurred) => logger.error(s"*** Model.add chemical error: $cause at: $occurred")
          case ChemicalSaved(id) =>
            observableChemicals += chemical.copy(id = id)
            selectedChemicalId.set(chemical.id)
          case _ => ()
    )

  def update(chemical: Chemical): Unit =
    Proxy.call(
      SaveChemical(observableAccount.get.license, chemical),
      (event: Event) =>
        event match
          case Fault(cause, occurred) => logger.error(s"*** Model.update chemical error: $cause at: $occurred")
          case ChemicalSaved(id) =>
            observableChemicals.update(observableChemicals.indexOf(chemical), chemical)
            selectedChemicalId.set(chemical.id)
          case _ => ()
    )

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

  def init: Unit =
    logger.info(s"*** Model: initializing ...")
    pools()
    logger.info(s"*** Model: initialized.")

  private def dashboard(): Unit =
    val numberFormat = NumberFormat.getNumberInstance()
    numberFormat.setMaximumFractionDigits(1)
    observableMeasurements.headOption.foreach { measurement =>
      onMeasurementChange(measurement, numberFormat)
      onAverageMeasurementChange(numberFormat)
    }

  private def onMeasurementChange(measurement: Measurement, numberFormat: NumberFormat): Unit =
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

  private def onAverageMeasurementChange(numberFormat: NumberFormat): Unit =
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