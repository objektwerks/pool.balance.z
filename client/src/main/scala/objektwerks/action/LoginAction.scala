package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

import objektwerks.{Login, Model}

final class LoginAction(name: String, login: Login) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit = Model.login(login.emailAddress, login.pin)