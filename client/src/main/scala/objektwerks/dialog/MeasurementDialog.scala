package objektwerks.dialog

import objektwerks.{Context, Measurement}
import objektwerks.action.{Actions, CancelAction, SaveMeasurementAction}
import objektwerks.field.{DoubleField, IntField, StringField}
import objektwerks.form.Form

final class MeasurementDialog(measurement: Measurement) extends Dialog(Context.measurement):
  var editedMeasurement = measurement.copy()

  val totalChlorine = IntField( measurement.totalChlorine, (value: Int) => editedMeasurement = measurement.copy(totalChlorine = value) )


  val cancelAction = CancelAction(Context.cancel, () => close())
  val saveAction = SaveMeasurementAction(Context.save, editedMeasurement)
  val actions = Actions(cancelAction, saveAction)