package objektwerks.dialog

import objektwerks.{Context, Measurement}
import objektwerks.action.{Actions, CancelAction, SaveMeasurementAction}

final class MeasurementDialog(measurement: Measurement) extends Dialog(Context.measurement):
  var editedMeasurement = measurement.copy()

  val cancelAction = CancelAction(Context.cancel, () => close())
  val saveAction = SaveMeasurementAction(Context.save, editedMeasurement)
  val actions = Actions(cancelAction, saveAction)