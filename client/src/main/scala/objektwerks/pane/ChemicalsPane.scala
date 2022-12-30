package objektwerks.pane

import java.awt.BorderLayout
import javax.swing.JPanel

import objektwerks.Model
import objektwerks.Context.*

final class ChemicalsPane extends JPanel:
  val chemicals = Model.observableChemicals.toList
  val columns = List(id, poolId, typeof, amount, unit, added)

  setLayout( new BorderLayout() )