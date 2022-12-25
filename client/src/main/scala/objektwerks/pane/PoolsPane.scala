package objektwerks.pane

import java.awt.BorderLayout
import javax.swing.JPanel

import objektwerks.{Context, Model, Pool}
import objektwerks.action.{Actions, AddPoolAction, EditPoolAction}
import objektwerks.table.Table
import javax.swing.event.{ListSelectionEvent, ListSelectionListener}

object PoolsPane:
  val columns = List("id", "name", "volume", "unit")
  val pools = Model.observablePools
  val table = Table[Pool](pools.toList, columns)

  table.getSelectionModel().addListSelectionListener(
    new ListSelectionListener {
      override def valueChanged(event: ListSelectionEvent): Unit =
        if !event.getValueIsAdjusting() then Model.selectedPoolId.value = event.getSource().asInstanceOf[Pool].id
    }
  )

  val addAction = AddPoolAction(Context.add, Context.addImageIcon)
  val editAction = EditPoolAction(Context.edit, Context.editImageIcon)
  val actions = Actions(addAction, editAction)

final class PoolsPane extends JPanel:
  import PoolsPane.*

  setLayout( new BorderLayout() )

  add(table, BorderLayout.CENTER)
  add(actions, BorderLayout.SOUTH)