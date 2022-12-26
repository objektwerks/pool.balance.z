package objektwerks.pane

import java.awt.BorderLayout
import java.awt.event.{ActionEvent, MouseAdapter, MouseEvent}
import javax.swing.{JPanel, JScrollPane}
import javax.swing.event.{ListSelectionEvent, ListSelectionListener}

import objektwerks.{Context, Model, Pool}
import objektwerks.action.{Actions, AddPoolAction, EditPoolAction}
import objektwerks.table.{ColumnModel, Table, TableModel}

final class PoolsPane extends JPanel:
  val pools = Model.observablePools.toList
  val columns = List("id", "name", "volume", "unit")
  val table = Table(
    TableModel(pools),
    ColumnModel(columns)
  )
  val scrollPane = new JScrollPane(table)

  table.getSelectionModel().addListSelectionListener(
    new ListSelectionListener {
      override def valueChanged(event: ListSelectionEvent): Unit =
        table
          .getId(event)
          .fold(())(id => Model.selectedPoolId.value = id)
    }
  )

  table.addMouseListener(
    new MouseAdapter {
      override def mouseClicked(event: MouseEvent): Unit =
        table
          .getId(event)
          .fold(())(id => editAction.actionPerformed( new ActionEvent(table, ActionEvent.ACTION_PERFORMED, id.toString) ))
    }
  )

  val addAction = AddPoolAction(Context.add)
  val editAction = EditPoolAction(Context.edit)
  val actions = Actions(addAction, editAction)
  
  setLayout( new BorderLayout() )

  add(scrollPane, BorderLayout.CENTER)
  add(actions, BorderLayout.SOUTH)