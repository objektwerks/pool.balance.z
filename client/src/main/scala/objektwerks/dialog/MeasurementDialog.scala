package objektwerks.dialog

import objektwerks.{Context, Measurement}

final class MeasurementDialog(measurement: Measurement) extends Dialog(Context.measurement):
  var editedMeasurement = measurement.copy()