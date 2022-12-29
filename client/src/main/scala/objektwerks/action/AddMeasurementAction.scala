package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

import objektwerks.dialog.MeasurementDialog
import objektwerks.{Measurement, Model}

final class AddMeasurementAction(name: String) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit = MeasurementDialog(Measurement(poolId = Model.selectedPoolId.get)).open()