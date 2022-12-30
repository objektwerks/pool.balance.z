package objektwerks.pane

import java.awt.BorderLayout
import javax.swing.JPanel

import objektwerks.Model

final class ChemicalsPane extends JPanel:
  val chemicals = Model.observableChemicals.toList

  setLayout( new BorderLayout() )