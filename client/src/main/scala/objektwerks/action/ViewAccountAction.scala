package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

import objektwerks.Model
import objektwerks.dialog.AccountDialog

final class ViewAccountAction(name: String) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit = AccountDialog( Model.observableAccount.get ).open()