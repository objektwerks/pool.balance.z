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
  val columns = List("Id", "Name", "Volume", "Unit")
  val table = Table(
    TableModel(pools),
    ColumnModel(columns),
    Long => setSelectedPoolId(Long),
    Long => fireEditAction(Long)
  )
  val tablePane = new JScrollPane(table)

  val addAction = AddPoolAction(Context.add)
  val editAction = EditPoolAction(Context.edit)
  val actions = Actions(addAction, editAction)

  def setSelectedPoolId(id: Long): Unit = Model.selectedPoolId.value = id

  def fireEditAction(id: Long): Unit = editAction.actionPerformed( new ActionEvent(table, ActionEvent.ACTION_PERFORMED, id.toString) )
  
  setLayout( new BorderLayout() )

  add(tablePane, BorderLayout.CENTER)
  add(actions, BorderLayout.SOUTH)