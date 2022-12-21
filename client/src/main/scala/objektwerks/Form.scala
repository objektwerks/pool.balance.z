package objektwerks

import java.awt.GridLayout
import javax.swing.{JComponent, JPanel}

object Form:
  val columns = 2
  val horizontalGap = 6
  val verticalGap = 6

class Form(fields: List[(String, JComponent)]) extends JPanel:
  import Form.*

  val rows = fields.length
  setLayout( new GridLayout(rows, columns, horizontalGap, verticalGap) )