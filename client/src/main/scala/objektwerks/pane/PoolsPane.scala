package objektwerks.pane

import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.event.{ListSelectionEvent, ListSelectionListener}

import objektwerks.{Context, Model, Pool}
import objektwerks.action.{Actions, AddPoolAction, EditPoolAction}
import objektwerks.table.Table

final class PoolsPane extends JPanel:
  val columns = List("id", "name", "volume", "unit")
  val pools = Model.observablePools
  val table = Table(columns, pools.toList)

  table.getSelectionModel().addListSelectionListener(
    new ListSelectionListener {
      override def valueChanged(event: ListSelectionEvent): Unit =
        table.getId(event).fold(())(id => Model.selectedPoolId.value = id)
    }
  )

  val addAction = AddPoolAction(Context.add)
  val editAction = EditPoolAction(Context.edit)
  val actions = Actions(addAction, editAction)
  
  setLayout( new BorderLayout() )

  add(table, BorderLayout.CENTER)
  add(actions, BorderLayout.SOUTH)