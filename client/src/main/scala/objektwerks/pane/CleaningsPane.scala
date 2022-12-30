package objektwerks.pane

import java.awt.BorderLayout
import javax.swing.JPanel

import objektwerks.Model

final class CleaningsPane extends JPanel:
  val cleanings = Model.observableCleanings.toList

  setLayout( new BorderLayout() )