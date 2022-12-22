package objektwerks.pane

import java.awt.GridLayout
import javax.swing.JPanel

object ChemicalsPane:
  val rows = 2
  val columns = 1
  val horizontalGap = 6
  val verticalGap = 6

final class ChemicalsPane extends JPanel:
  import ChemicalsPane.*

  setLayout( new GridLayout(rows, columns, horizontalGap, verticalGap) )