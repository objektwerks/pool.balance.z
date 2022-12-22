package objektwerks.dashboard

import java.awt.GridLayout
import javax.swing.JPanel

object TotalChlorinePane:
  val rows = 5
  val columns = 2
  val horizontalGap = 6
  val verticalGap = 6

final class TotalChlorinePane extends JPanel:
  import TotalChlorinePane.*

  setLayout( new GridLayout(rows, columns, horizontalGap, verticalGap) )