package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

import objektwerks.dialog.ChemicalDialog
import objektwerks.{Chemical, Model}

final class AddChemicalAction(name: String) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit = ChemicalDialog(Chemical(poolId = Model.selectedPoolId.get)).open()