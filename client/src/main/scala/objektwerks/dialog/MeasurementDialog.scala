package objektwerks.dialog

import javax.swing.{JComponent, JLabel}

import objektwerks.{Context, Measurement}
import objektwerks.action.{Actions, CancelAction, SaveMeasurementAction}
import objektwerks.field.{DoubleField, IntField, StringField}
import objektwerks.form.Form

final class MeasurementDialog(measurement: Measurement) extends Dialog(Context.measurement):
  var editedMeasurement = measurement.copy()

  val totalChlorine = IntField( measurement.totalChlorine, (value: Int) => editedMeasurement = measurement.copy(totalChlorine = value) )
  val freeChlorine = IntField( measurement.freeChlorine, (value: Int) => editedMeasurement = measurement.copy(freeChlorine = value) )
  val combinedChlorine = DoubleField( measurement.combinedChlorine, (value: Double) => editedMeasurement = measurement.copy(combinedChlorine = value) )
  val ph = DoubleField( measurement.ph, (value: Double) => editedMeasurement = measurement.copy(ph = value) )
  val calciumHardness = IntField( measurement.calciumHardness, (value: Int) => editedMeasurement = measurement.copy(calciumHardness = value) )
  val totalAlkalinity = IntField( measurement.totalAlkalinity, (value: Int) => editedMeasurement = measurement.copy(totalAlkalinity = value) )
  val cyanuricAcid = IntField( measurement.cyanuricAcid, (value: Int) => editedMeasurement = measurement.copy(cyanuricAcid = value) )

  val form = Form(
    List[(String, JComponent)](
      "Total Chlorine:" -> totalChlorine,
      "Free Chlorine:" -> freeChlorine,
      "Combined Chlorine:" -> combinedChlorine,
      "Ph:" -> ph,
      "Calcium Hardness:" -> calciumHardness,
      "Total Alkalinity:" -> totalAlkalinity,
      "Cyanuric Acid:" -> cyanuricAcid
    )
  )

  val cancelAction = CancelAction(Context.cancel, () => close())
  val saveAction = SaveMeasurementAction(Context.save, editedMeasurement)
  val actions = Actions(cancelAction, saveAction)