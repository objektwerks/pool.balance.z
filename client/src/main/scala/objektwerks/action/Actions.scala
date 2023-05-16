package objektwerks.action

import java.awt.Dimension
import javax.swing.{Action, BoxLayout, JButton, JPanel}

final class Actions(actions: Action*) extends JPanel:
  val columns = actions.length
  setLayout( BoxLayout(this, BoxLayout.LINE_AXIS) )

  for (action <- actions)
    val button = JButton(action)
    button.setPreferredSize( Dimension(80, 40) )
    add(button)