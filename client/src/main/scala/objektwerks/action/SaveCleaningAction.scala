package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

import objektwerks.{Model, Cleaning}

final class SaveCleaningAction(name: String,
                           cleaning: Cleaning) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit = 
    if cleaning.id == 0 then Model.add(cleaning) else Model.update(cleaning)