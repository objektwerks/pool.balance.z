package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

final class CancelAction(name: String,
                         cancel: () => Unit) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit = cancel()