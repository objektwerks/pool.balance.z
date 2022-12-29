package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

import objektwerks.{Model, Measurement}

final class SaveMeasurementAction(name: String,
                                  measurement: Measurement) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit = 
    if measurement.id == 0 then Model.add(measurement) else Model.update(measurement)