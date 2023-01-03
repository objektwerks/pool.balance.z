package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

import objektwerks.dialog.FaultsDialog

final class ViewFaultstAction(name: String) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit = FaultsDialog().open()