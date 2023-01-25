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

  observableAccount.onChange { (_, oldAccount, newAccount) =>
    logger.info(s"*** Model: selected account onchange event: $oldAccount -> $newAccount")
  }

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

  def init: Unit = // TODO Enable once showstopper bug fixed!!!
    logger.info(s"*** Model: initializing ...")
    pools()
    logger.info(s"*** Model: initialized.")

  def onFault(source: String, fault: Fault): Unit =
    observableFaults += fault
    logger.error(s"*** $source - $fault")

  def onFault(source: String, entity: Entity, fault: Fault): Unit =
    observableFaults += fault
    logger.error(s"*** $source - $entity - $fault")

  def currentPool: Option[Pool] = observablePools.find( pool => pool.id == selectedPoolId.get )

  def currentCleaning: Option[Cleaning] = observableCleanings.find( cleaning => cleaning.id == selectedCleaningId.get )

  def currentMeasurement: Option[Measurement] = observableMeasurements.find( measurement => measurement.id == selectedMeasurementId.get )

  def currentChemical: Option[Chemical] = observableChemicals.find( chemical => chemical.id == selectedChemicalId.get )

  def register(emailAddress: String): Unit =
    Proxy.call(
      Register(emailAddress),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.register", fault)
        case Registered(account) => observableAccount.set(account)
        case _ => ()
    )

  def login(emailAddress: String, pin: String): Unit =
    Proxy.call(
      Login(emailAddress, pin),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.login", fault)
        case LoggedIn(account) => observableAccount.set(account)
        case _ => ()
    )

  def deactivate(license: String): Unit =
    Proxy.call(
      Deactivate(license),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.deactivate", fault)
        case Deactivated(account) => observableAccount.set(account)
        case _ => ()
    )

  def reactivate(license: String): Unit =
    Proxy.call(
      Reactivate(license),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.reactivate", fault)
        case Reactivated(account) => observableAccount.set(account)
        case _ => ()
    )

  def pools(): Unit =
    Proxy.call(
      ListPools(observableAccount.get.license),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.pools", fault)
        case PoolsListed(pools) =>
          observablePools.clear()
          observablePools ++= pools
        case _ => ()
    )

  def save(pool: Pool): Unit =
    Proxy.call(
      SavePool(observableAccount.get.license, pool),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.save pool", pool, fault)
        case PoolSaved(id) =>
          if pool.id == 0 then observablePools += pool.copy(id = id)
          else observablePools.update(observablePools.indexOf(pool), pool)
          observablePools.sort()
          selectedPoolId.set(pool.id)
        case _ => ()
    )

  def cleanings(poolId: Long): Unit =
    Proxy.call(
      ListCleanings(observableAccount.get.license, poolId),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.cleanings", fault)
        case CleaningsListed(cleanings) =>
          observableCleanings.clear()
          observableCleanings ++= cleanings
        case _ => ()
    )

  def save(cleaning: Cleaning): Unit =
    Proxy.call(
      SaveCleaning(observableAccount.get.license, cleaning),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.save cleaning", cleaning, fault)
        case CleaningSaved(id) =>
          if cleaning.id == 0 then observableCleanings += cleaning.copy(id = id)
          else observableCleanings.update(observableCleanings.indexOf(cleaning), cleaning)
          observableCleanings.sort()
          selectedCleaningId.set(cleaning.id)
        case _ => ()
    )

  def measurements(poolId: Long): Unit =
    Proxy.call(
      ListMeasurements(observableAccount.get.license, poolId),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.measurements", fault)
        case MeasurementsListed(measurements) =>
          observableMeasurements.clear()
          observableMeasurements ++= measurements
        case _ => ()
    )

  def save(measurement: Measurement): Unit =
    Proxy.call(
      SaveMeasurement(observableAccount.get.license, measurement),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.save measurement", measurement, fault)
        case MeasurementSaved(id) =>
          if measurement.id == 0 then observableMeasurements += measurement.copy(id = id)
          else observableMeasurements.update(observableMeasurements.indexOf(measurement), measurement)
          observableMeasurements.sort()
          selectedMeasurementId.set(measurement.id)
        case _ => ()
    )

  def chemicals(poolId: Long): Unit =
    Proxy.call(
      ListChemicals(observableAccount.get.license, poolId),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.chemicals", fault)
        case ChemicalsListed(chemicals) =>
          observableChemicals.clear()
          observableChemicals ++= chemicals
        case _ => ()
    )
  
  def save(chemical: Chemical): Unit =
    Proxy.call(
      SaveChemical(observableAccount.get.license, chemical),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.save chemical", chemical, fault)
        case ChemicalSaved(id) =>
          if chemical.id == 0 then observableChemicals += chemical.copy(id = id)
          else observableChemicals.update(observableChemicals.indexOf(chemical), chemical)
          observableChemicals.sort()
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

  private def dashboard(): Unit =
    val numberFormat = NumberFormat.getNumberInstance()
    numberFormat.setMaximumFractionDigits(1)
    observableMeasurements.headOption.foreach { measurement =>
      calculateCurrentMeasurements(measurement, numberFormat)
      calculateAverageMeasurements(numberFormat)
    }

  private def calculateCurrentMeasurements(measurement: Measurement, numberFormat: NumberFormat): Unit =
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

  private def calculateAverageMeasurements(numberFormat: NumberFormat): Unit =
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