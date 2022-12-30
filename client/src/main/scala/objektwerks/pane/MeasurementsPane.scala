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

  setLayout( new BorderLayout() )