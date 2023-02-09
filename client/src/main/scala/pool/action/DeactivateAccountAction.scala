package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

import objektwerks.Model

final class DeactivateAccountAction(name: String) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit = Model.deactivate( Model.observableAccount.get.license )