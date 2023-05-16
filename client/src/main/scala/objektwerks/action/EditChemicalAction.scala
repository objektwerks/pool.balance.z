package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

import objektwerks.dialog.ChemicalDialog
import objektwerks.Model

final class EditChemicalAction(name: String) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit = Model.currentChemical.fold(())(chemical => ChemicalDialog(chemical).open())