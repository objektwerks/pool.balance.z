package objektwerks.dialog

import java.awt.BorderLayout
import javax.swing.{JDialog, JTextField}

import objektwerks.{Context, Login}
import objektwerks.action.{Actions, LoginAction, RegisterAction}
import objektwerks.field.StringField
import objektwerks.form.Form
import objektwerks.Register

// build a login and register tab
final class LoginRegisterDialog extends JDialog:
  setTitle(Context.title)
  setLayout( new BorderLayout() )
  setLocationRelativeTo(null)
  setModal(true)

  var login = Login("", "")

  val loginEmailAddress = StringField(
    "",
    36,
    (value: String) => login = login.copy(emailAddress = value)
  )

  val pin = StringField(
    "",
    7,
    (value: String) => login = login.copy(pin = value)
  )

  val loginForm = Form(
    List[(String, JTextField)](
      Context.emailAddress -> loginEmailAddress,
      Context.password -> pin,
    )
  )

  val loginAction = LoginAction(Context.login, login)
  val loginActions = Actions(loginAction)

  var register = Register("")

  val registerEmailAddress = StringField(
    "",
    36,
    (value: String) => register = register.copy(emailAddress = value)
  )

  val registerForm = Form(
    List[(String, JTextField)](
      Context.emailAddress -> registerEmailAddress
    )
  )

  val registerAction = RegisterAction(Context.register, register)
  val registerActions = Actions(loginAction)