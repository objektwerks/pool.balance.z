package objektwerks.action

import java.awt.{Dimension, GridLayout}
import javax.swing.{Action, JButton, JPanel}

object Actions:
  val rows = 1
  val horizontalGap = 6
  val verticalGap = 6

final class Actions(actions: Action*) extends JPanel:
  import Actions.*

  val columns = actions.length
  setLayout( new GridLayout(rows, columns, horizontalGap, verticalGap) )

  for (action <- actions)
    val button = new JButton(action)
    button.setPreferredSize( new Dimension(80, 40) )
    add(button)