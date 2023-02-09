package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

import objektwerks.{Model, Pool}
import objektwerks.dialog.PoolDialog

final class AddPoolAction(name: String) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit = PoolDialog(Pool(license = Model.observableAccount.get.license)).open()