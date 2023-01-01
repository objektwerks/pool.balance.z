package objektwerks.pane

import java.awt.BorderLayout
import java.awt.event.ActionEvent
import javax.swing.{JPanel, JScrollPane}

import objektwerks.{Context, Model}
import objektwerks.Context.*
import objektwerks.action.{Actions, AddChemicalAction, EditChemicalAction}
import objektwerks.table.{ColumnModel, Table, TableModel}

final class ChemicalsPane extends JPanel:
  val chemicals = Model.observableChemicals.toList
  val columns = List(id, poolId, typeof, amount, unit, added)
  val table = Table(
    TableModel(chemicals),
    ColumnModel(columns),
    Long => setSelectedId(Long),
    Long => fireEditActionById(Long)
  )
  Model.observableChemicals.onChange { (_, _) =>
    table.setModel( TableModel( Model.observableChemicals.toList ) )
  }
  val tablePane = new JScrollPane(table)

  val addAction = AddChemicalAction(Context.add)
  val editAction = EditChemicalAction(Context.edit)
  val actions = Actions(addAction, editAction)

  def setSelectedId(id: Long): Unit = Model.selectedChemicalId.value = id

  def fireEditActionById(id: Long): Unit = editAction.actionPerformed( new ActionEvent(table, ActionEvent.ACTION_PERFORMED, id.toString) )

  setLayout( new BorderLayout() )

  add(tablePane, BorderLayout.CENTER)
  add(actions, BorderLayout.SOUTH)