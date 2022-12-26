package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

import objektwerks.{Model, Pool}

final class SavePoolAction(name: String,
                           pool: Pool) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit =
    if pool.id == 0 then Model.add(pool) else Model.update(pool)