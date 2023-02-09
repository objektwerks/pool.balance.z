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
  val columns = List(id, poolId, brush, net, skimmerBasket.asHtmlWrap, pumpBasket.asHtmlWrap, pumpFilter.asHtmlWrap, vacuum, cleaned)
  val table = Table(
    TableModel(cleanings),
    ColumnModel(columns),
    Long => setSelectedId(Long),
    Long => fireEditActionById(Long)
  )
  Model.observableCleanings.onChange { (_, _) =>
    table.setModel( TableModel( Model.observableCleanings.toList ) )
  }
  val tablePane = JScrollPane(table)

  val addAction = AddCleaningAction(Context.add)
  val editAction = EditCleaningAction(Context.edit)
  val actions = Actions(addAction, editAction)

  def setSelectedId(id: Long): Unit = Model.selectedCleaningId.value = id

  def fireEditActionById(id: Long): Unit = editAction.actionPerformed( ActionEvent(table, ActionEvent.ACTION_PERFORMED, id.toString) )

  setLayout( BorderLayout() )

  add(tablePane, BorderLayout.CENTER)
  add(actions, BorderLayout.SOUTH)