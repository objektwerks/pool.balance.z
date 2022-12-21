package objektwerks

import java.awt.GridLayout
import javax.swing.{JButton, JPanel}

object Actions:
  val rows = 1
  val horizontalGap = 6
  val verticalGap = 6

final class Actions(buttons: JButton*) extends JPanel:
  import Actions.*

  val columns = buttons.length
  setLayout( new GridLayout(rows, columns, horizontalGap, verticalGap) )