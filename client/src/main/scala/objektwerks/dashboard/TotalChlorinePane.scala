package objektwerks.dashboard

import java.awt.GridLayout
import javax.swing.JPanel
import javax.swing.border.TitledBorder

object TotalChlorinePane:
  val rows = 1
  val columns = 1
  val horizontalGap = 6
  val verticalGap = 6

final class TotalChlorinePane extends JPanel:
  import TotalChlorinePane.*

  setLayout( new GridLayout(rows, columns, horizontalGap, verticalGap) )
  setBorder( new TitledBorder("<html>Total<br>Chlorine") )