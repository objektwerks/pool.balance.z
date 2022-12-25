package objektwerks.pane

import java.awt.BorderLayout
import javax.swing.JPanel

import objektwerks.{Context, Model, Pool}
import objektwerks.action.{Actions, AddPoolAction, EditPoolAction}
import objektwerks.table.Table

object PoolsPane:
  val columns = List("name", "volume", "unit")
  val pools = Model.observablePools
  val table = Table[Pool](pools.toList, columns)

  val addAction = AddPoolAction(Context.add, Context.addImageIcon)
  val editAction = EditPoolAction(Context.edit, Context.editImageIcon)
  val actions = Actions(addAction, editAction)

final class PoolsPane extends JPanel:
  import PoolsPane.*

  setLayout( new BorderLayout() )

  add(table, BorderLayout.CENTER)
  add(actions, BorderLayout.SOUTH)