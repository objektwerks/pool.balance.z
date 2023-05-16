package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

import objektwerks.dialog.CleaningDialog
import objektwerks.Model

final class EditCleaningAction(name: String) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit = Model.currentCleaning.fold(())(cleaning => CleaningDialog(cleaning).open())