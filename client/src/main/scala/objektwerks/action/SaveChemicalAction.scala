package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

import objektwerks.{Model, Chemical}

final class SaveChemicalAction(name: String,
                               chemical: Chemical) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit = 
    if chemical.id == 0 then Model.add(chemical) else Model.update(chemical)