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

  setLayout( new BorderLayout() )