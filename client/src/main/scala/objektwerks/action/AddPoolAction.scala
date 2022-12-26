package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

import objektwerks.Context
import objektwerks.dialog.PoolDialog
import objektwerks.Pool

final class AddPoolAction(name: String) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit = PoolDialog(Context.pool, Pool()).view(true)