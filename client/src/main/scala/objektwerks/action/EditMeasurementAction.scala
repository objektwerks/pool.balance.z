package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

import objektwerks.dialog.MeasurementDialog
import objektwerks.Model

final class EditMeasurementAction(name: String) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit = Model.currentMeasurement.fold(())(measurement => MeasurementDialog(measurement).open())