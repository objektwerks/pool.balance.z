package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

import objektwerks.{Model, Register}

final class RegisterAction(name: String, register: Register) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit = Model.register(register.emailAddress)