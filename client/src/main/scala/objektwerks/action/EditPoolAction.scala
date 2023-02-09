package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

import objektwerks.dialog.PoolDialog
import objektwerks.{Model, Pool}

final class EditPoolAction(name: String) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit = Model.currentPool.fold(())(pool => PoolDialog(pool).open())