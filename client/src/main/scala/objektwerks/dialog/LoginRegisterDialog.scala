package objektwerks.dialog

import javax.swing.JTextField

import objektwerks.{Context, Login}
import objektwerks.field.StringField
import objektwerks.form.Form
import objektwerks.Register

// build login and register form
// build login and register action
// build a login and register tab
final class LoginRegisterDialog extends Dialog(Context.title):
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