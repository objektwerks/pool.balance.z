package objektwerks.pane

import java.awt.GridLayout
import javax.swing.JPanel

object MeasurementsPane:
  val rows = 2
  val columns = 1
  val horizontalGap = 6
  val verticalGap = 6

final class MeasurementsPane extends JPanel:
  import MeasurementsPane.*

  setLayout( new GridLayout(rows, columns, horizontalGap, verticalGap) )