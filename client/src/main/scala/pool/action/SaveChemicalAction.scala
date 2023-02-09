package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

import objektwerks.{Model, Chemical}

final class SaveChemicalAction(name: String,
                               chemical: Chemical) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit = Model.save(chemical)