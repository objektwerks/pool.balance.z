package objektwerks.pane

import java.awt.BorderLayout
import java.awt.event.ActionEvent
import javax.swing.{JPanel, JScrollPane}

import objektwerks.{Context, Model}
import objektwerks.Context.*
import objektwerks.action.{Actions, AddCleaningAction, EditCleaningAction}
import objektwerks.table.{ColumnModel, Table, TableModel}

final class CleaningsPane extends JPanel:
  val cleanings = Model.observableCleanings.toList
  val columns = List(id, poolId, brush, net, skimmerBasket.wrap, pumpBasket.wrap, pumpFilter.wrap, vacuum, cleaned)
  val table = Table(
    TableModel(cleanings),
    ColumnModel(columns),
    Long => setSelectedId(Long),
    Long => fireEditActionById(Long)
  )
  val tablePane = new JScrollPane(table)

  val addAction = AddCleaningAction(Context.add)
  val editAction = EditCleaningAction(Context.edit)
  val actions = Actions(addAction, editAction)

  def setSelectedId(id: Long): Unit = Model.selectedCleaningId.value = id

  def fireEditActionById(id: Long): Unit = editAction.actionPerformed( new ActionEvent(table, ActionEvent.ACTION_PERFORMED, id.toString) )

  setLayout( new BorderLayout() )

  add(tablePane, BorderLayout.CENTER)
  add(actions, BorderLayout.SOUTH)