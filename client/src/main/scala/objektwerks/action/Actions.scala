package objektwerks.action

import java.awt.Dimension
import javax.swing.{Action, BoxLayout, JButton, JPanel}
import javax.swing.BoxLayout

final class Actions(actions: Action*) extends JPanel:
  val columns = actions.length
  setLayout( new BoxLayout(this, BoxLayout.LINE_AXIS) )

  for (action <- actions)
    val button = new JButton(action)
    button.setPreferredSize( new Dimension(80, 40) )
    add(button)