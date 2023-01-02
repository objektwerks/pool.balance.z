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
    id, poolId, totalChlorine.asHtmlWrap, freeChlorine.asHtmlWrap, combinedChlorine.asHtmlWrap, ph, calciumHardness.asHtmlWrap,
    totalAlkalinity.asHtmlWrap, cyanuricAcid.asHtmlWrap, totalBromine.asHtmlWrap, salt, temperature, measured
  )
  val table = Table(
    TableModel(measurements),
    ColumnModel(columns),
    Long => setSelectedId(Long),
    Long => fireEditActionById(Long)
  )
  Model.observableMeasurements.onChange { (_, _) =>
    table.setModel( TableModel( Model.observableMeasurements.toList ) )
  }
  val tablePane = new JScrollPane(table)

  val addAction = AddMeasurementAction(Context.add)
  val editAction = EditMeasurementAction(Context.edit)
  val actions = Actions(addAction, editAction)

  def setSelectedId(id: Long): Unit = Model.selectedMeasurementId.value = id

  def fireEditActionById(id: Long): Unit = editAction.actionPerformed( new ActionEvent(table, ActionEvent.ACTION_PERFORMED, id.toString) )

  setLayout( new BorderLayout() )

  add(tablePane, BorderLayout.CENTER)
  add(actions, BorderLayout.SOUTH)