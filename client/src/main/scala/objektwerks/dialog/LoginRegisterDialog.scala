package objektwerks.dialog

import java.awt.BorderLayout
import javax.swing.{BoxLayout, JDialog, JPanel, JTextField}
import javax.swing.border.TitledBorder

import objektwerks.{Context, Login, Register}
import objektwerks.action.{Actions, LoginAction, RegisterAction}
import objektwerks.field.StringField
import objektwerks.form.Form

final class LoginRegisterDialog() extends JDialog():
  setTitle(Context.title)
  setLayout( BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS) )
  setSize(380, 240)
  setLocationRelativeTo(null)
  setModal(true)

  def open(): Unit = setVisible(true)

  def close(): Unit =
    setVisible(false)
    dispose()

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

  val registerPane = JPanel()
  val registerTitleBorder = TitledBorder(Context.register)
  registerTitleBorder.setTitleColor(Context.fuchsia)
  registerPane.setBorder(registerTitleBorder)
  registerPane.setLayout( BoxLayout(registerPane, BoxLayout.PAGE_AXIS) )
  registerPane.add(registerForm)

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

  val loginPane = JPanel()
  val loginTitleBorder = TitledBorder(Context.login)
  loginTitleBorder.setTitleColor(Context.fuchsia)
  loginPane.setBorder(loginTitleBorder)
  loginPane.setLayout( BoxLayout(loginPane, BoxLayout.PAGE_AXIS) )
  loginPane.add(loginForm)

  val registerAction = RegisterAction(Context.register, register)
  val loginAction = LoginAction(Context.login, login)
  val actions = Actions(registerAction, loginAction)

  add(registerPane)
  add(loginPane)
  add(actions)