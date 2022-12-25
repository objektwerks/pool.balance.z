package objektwerks

import java.awt.GridLayout
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
    add( new JButton(action) )