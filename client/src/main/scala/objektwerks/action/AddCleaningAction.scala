package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

import objektwerks.dialog.CleaningDialog
import objektwerks.{Cleaning, Model}

final class AddCleaningAction(name: String) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit = CleaningDialog(Cleaning(poolId = Model.selectedPoolId.get)).open()