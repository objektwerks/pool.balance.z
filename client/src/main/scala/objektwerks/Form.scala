package objektwerks

import java.awt.GridLayout
import javax.swing.{BorderFactory, JComponent, JLabel, JPanel}

object Form:
  val columns = 2
  val horizontalGap = 6
  val verticalGap = 6
  val inset = 6

class Form(fields: List[(String, JComponent)]) extends JPanel:
  import Form.*

  val rows = fields.length
  setLayout( new GridLayout(rows, columns, horizontalGap, verticalGap) )
  setBorder( BorderFactory.createEmptyBorder(inset, inset, inset, inset) )

  for ((label, component) <- fields)
    add( new JLabel(label) )
    add( component )
