package objektwerks.form

import java.awt.GridLayout
import javax.swing.{BorderFactory, JComponent, JLabel, JPanel}

object Form:
  val columns = 2
  val horizontalGap = 3
  val verticalGap = 3

final class Form(fields: List[(String, JComponent)]) extends JPanel:
  import Form.*

  private val rows = fields.length
  setLayout( new GridLayout(rows, columns, horizontalGap, verticalGap) )
  setBorder( BorderFactory.createEmptyBorder(3, 3, 3, 3) )

  for ( (label, component) <- fields )
    add( new JLabel(label) )
    add( component )