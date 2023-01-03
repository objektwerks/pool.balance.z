package objektwerks.pane

import java.awt.BorderLayout
import java.awt.event.ActionEvent
import javax.swing.{JPanel, JScrollPane}
import javax.swing.event.{ListSelectionEvent, ListSelectionListener}

import objektwerks.{Context, Model, Pool}
import objektwerks.Context.*
import objektwerks.action.{Actions, AddPoolAction, EditPoolAction, ViewAccountAction, ViewFaultstAction}
import objektwerks.table.{ColumnModel, Table, TableModel}

final class PoolsPane extends JPanel:
  val pools = Model.observablePools.toList
  val columns = List(id, name, volume, unit)
  val table = Table(
    TableModel(pools),
    ColumnModel(columns),
    Long => setSelectedId(Long),
    Long => fireEditActionById(Long)
  )
  Model.observablePools.onChange { (_, _) =>
    table.setModel( TableModel( Model.observablePools.toList ) )
  }
  val tablePane = new JScrollPane(table)

  val addPoolAction = AddPoolAction(Context.add)
  val editPoolAction = EditPoolAction(Context.edit)
  val viewAccountAction = ViewAccountAction(Context.account)
  val viewFaultsAction = ViewFaultstAction("")
  val actions = Actions(addPoolAction, editPoolAction, viewAccountAction, viewFaultsAction)

  def setSelectedId(id: Long): Unit = Model.selectedPoolId.value = id

  def fireEditActionById(id: Long): Unit = editPoolAction.actionPerformed( new ActionEvent(table, ActionEvent.ACTION_PERFORMED, id.toString) )
  
  setLayout( new BorderLayout() )

  add(tablePane, BorderLayout.CENTER)
  add(actions, BorderLayout.SOUTH)