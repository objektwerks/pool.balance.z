package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

final class CloseAction(name: String,
                        close: () => Unit) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit = close()