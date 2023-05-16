package objektwerks.dialog

import java.time.LocalDate
import javax.swing.{JComponent, JLabel}

import objektwerks.{Context, Measurement}
import objektwerks.Context.asLabel
import objektwerks.Measurement.*
import objektwerks.action.{Actions, CancelAction, SaveMeasurementAction}
import objektwerks.field.{DoubleRangeField, IntRangeField}
import objektwerks.form.Form

final class MeasurementDialog(measurement: Measurement) extends Dialog(Context.measurement):
  var editedMeasurement = measurement.copy()    

  val totalChlorine = IntRangeField(
    measurement.totalChlorine,
    (value: Int) => totalChlorineRange.contains(value),
    (value: Int) => editedMeasurement = measurement.copy(totalChlorine = value)
  )
  val freeChlorine = IntRangeField(
    measurement.freeChlorine,
    (value: Int) => freeChlorineRange.contains(value),
    (value: Int) => editedMeasurement = measurement.copy(freeChlorine = value)
  )
  val combinedChlorine = DoubleRangeField(
    measurement.combinedChlorine,
    (value: Double) => combinedChlorineRange.contains(value),
    (value: Double) => editedMeasurement = measurement.copy(combinedChlorine = value)
  )
  val ph = DoubleRangeField(
    measurement.ph,
    (value: Double) => phRange.contains(value),
    (value: Double) => editedMeasurement = measurement.copy(ph = value)
  )
  val calciumHardness = IntRangeField(
    measurement.calciumHardness,
    (value: Int) => calciumHardnessRange.contains(value),
    (value: Int) => editedMeasurement = measurement.copy(calciumHardness = value)
  )
  val totalAlkalinity = IntRangeField(
    measurement.totalAlkalinity,
    (value: Int) => totalAlkalinityRange.contains(value),
    (value: Int) => editedMeasurement = measurement.copy(totalAlkalinity = value)
  )
  val cyanuricAcid = IntRangeField(
    measurement.cyanuricAcid,
    (value: Int) => cyanuricAcidRange.contains(value),
    (value: Int) => editedMeasurement = measurement.copy(cyanuricAcid = value)
  )
  val totalBromine = IntRangeField(
    measurement.totalBromine,
    (value: Int) => totalBromineRange.contains(value),
    (value: Int) => editedMeasurement = measurement.copy(totalBromine = value)
  )
  val salt = IntRangeField(
    measurement.salt,
    (value: Int) => saltRange.contains(value),
    (value: Int) => editedMeasurement = measurement.copy(salt = value)
  )
  val temperature = IntRangeField(
    measurement.temperature,
    (value: Int) => temperatureRange.contains(value),
    (value: Int) => editedMeasurement = measurement.copy(temperature = value)
  )

  val form = Form(
    List[(String, JComponent)](
      Context.totalChlorine.asLabel -> totalChlorine,
      Context.freeChlorine.asLabel -> freeChlorine,
      Context.combinedChlorine.asLabel -> combinedChlorine,
      Context.ph.asLabel -> ph,
      Context.calciumHardness.asLabel -> calciumHardness,
      Context.totalAlkalinity.asLabel -> totalAlkalinity,
      Context.cyanuricAcid.asLabel -> cyanuricAcid,
      Context.totalBromine.asLabel -> totalBromine,
      Context.salt.asLabel -> salt,
      Context.temperature.asLabel -> temperature,
      Context.measured.asLabel -> JLabel( LocalDate.ofEpochDay(measurement.measured).toString )
    )
  )

  val cancelAction = CancelAction(Context.cancel, () => close())
  val saveAction = SaveMeasurementAction(Context.save, editedMeasurement)
  val actions = Actions(cancelAction, saveAction)

  add(form, actions)