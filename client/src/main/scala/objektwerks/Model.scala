package objektwerks

import scalafx.collections.ObservableBuffer
import scalafx.beans.property.ObjectProperty

object Model:
  val pools = ObservableBuffer[Pool]()
  val cleanings = ObservableBuffer[Cleaning]()
  val measurements = ObservableBuffer[Measurement]()
  val chemicals = ObservableBuffer[Chemical]()

  val selectedPoolId = ObjectProperty[Long](0)
  val selectedCleaningId = ObjectProperty[Long](0)
  val selectedMeasurementId = ObjectProperty[Long](0)
  val selectedChemicalId = ObjectProperty[Long](0)