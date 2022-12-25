package objektwerks.pane

import java.awt.BorderLayout
import javax.swing.JPanel

import objektwerks.{Context, Model, Pool}
import objektwerks.action.{Actions, AddPoolAction, EditPoolAction}
import objektwerks.table.Table
import javax.swing.event.{ListSelectionEvent, ListSelectionListener, ListSelectionModel}
import javax.swing.ListSelectionModel

object PoolsPane:
  val columns = List("id", "name", "volume", "unit")
  val pools = Model.observablePools
  val table = Table[Pool](pools.toList, columns)

  table.getSelectionModel().addListSelectionListener(
    new ListSelectionListener {
      override def valueChanged(event: ListSelectionEvent): Unit =
        if !event.getValueIsAdjusting() then
          val row = table.getSelectedRow()
          val column = 0
          val id = table.getModel().getValueAt(row, column).asInstanceOf[Long]
          Model.selectedPoolId.value = id
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