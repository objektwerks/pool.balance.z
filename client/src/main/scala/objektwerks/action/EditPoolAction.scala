package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.{AbstractAction, ImageIcon}

class EditPoolAction(name: String, image: ImageIcon) extends AbstractAction(name, image):
  override def actionPerformed(event: ActionEvent): Unit = ???