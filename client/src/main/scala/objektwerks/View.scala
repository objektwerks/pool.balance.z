package objektwerks

import java.awt.GridLayout
import javax.swing.JPanel

object View:
  val rows = 2
  val columns = 1
  val horizontalGap = 6
  val verticalGap = 6

final class View extends JPanel:
  import View.*

  setLayout( new GridLayout(rows, columns, horizontalGap, verticalGap) )