package objektwerks.pane

import java.awt.BorderLayout
import java.awt.event.ActionEvent
import javax.swing.{JPanel, JScrollPane}

import objektwerks.{Context, Model}
import objektwerks.Context.*
import objektwerks.action.{Actions, AddMeasurementAction, EditMeasurementAction}
import objektwerks.table.{ColumnModel, Table, TableModel}

final class MeasurementsPane extends JPanel:
  val measurements = Model.observableMeasurements.toList
  val columns = List(
    id, poolId, totalChlorine, freeChlorine, combinedChlorine, ph, calciumHardness,
    totalAlkalinity, cyanuricAcid, totalBromine, salt, temperature, measured
  )
  val table = Table(
    TableModel(measurements),
    ColumnModel(columns),
    Long => setSelectedId(Long),
    Long => fireEditActionById(Long)
  )
  val tablePane = new JScrollPane(table)

  val addAction = AddMeasurementAction(Context.add)
  val editAction = EditMeasurementAction(Context.edit)
  val actions = Actions(addAction, editAction)

  def setSelectedId(id: Long): Unit = Model.selectedCleaningId.value = id

  def fireEditActionById(id: Long): Unit = editAction.actionPerformed( new ActionEvent(table, ActionEvent.ACTION_PERFORMED, id.toString) )

  setLayout( new BorderLayout() )

  add(tablePane, BorderLayout.CENTER)
  add(actions, BorderLayout.SOUTH)