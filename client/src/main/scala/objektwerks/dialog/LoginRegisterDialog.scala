package objektwerks.dialog

import java.awt.{BorderLayout, Dimension}
import javax.swing.{BoxLayout, JDialog, JPanel, JTabbedPane, JTextField}

import objektwerks.{Context, Login, Register}
import objektwerks.action.{Actions, LoginAction, RegisterAction}
import objektwerks.field.StringField
import objektwerks.form.Form

final class LoginRegisterDialog() extends JDialog():
  setTitle(Context.title)
  setLayout( BorderLayout() )
  setSize(380, 180)
  setLocationRelativeTo(null)
  setModal(true)

  def open(): Unit = setVisible(true)

  def close(): Unit =
    setVisible(false)
    dispose()

  var login = Login("", "")

  val loginEmailAddress = StringField(
    "",
    36,
    (value: String) => login = login.copy(emailAddress = value)
  )
  loginEmailAddress.setPreferredSize( Dimension( 100, 40) )

  val pin = StringField(
    "",
    7,
    (value: String) => login = login.copy(pin = value)
  )
  pin.setPreferredSize( Dimension( 100, 40) )

  val loginForm = Form(
    List[(String, JTextField)](
      Context.emailAddress -> loginEmailAddress,
      Context.password -> pin,
    )
  )

  val loginAction = LoginAction(Context.login, login)
  val loginActions = Actions(loginAction)

  val loginPane = JPanel()
  loginPane.setLayout( BoxLayout(loginPane, BoxLayout.PAGE_AXIS) )
  loginPane.add(loginForm)
  loginPane.add(loginActions)

  var register = Register("")

  val registerEmailAddress = StringField(
    "",
    36,
    (value: String) => register = register.copy(emailAddress = value)
  )
  registerEmailAddress.setPreferredSize( Dimension( 100, 40) )

  val registerForm = Form(
    List[(String, JTextField)](
      Context.emailAddress -> registerEmailAddress
    )
  )

  val registerAction = RegisterAction(Context.register, register)
  val registerActions = Actions(registerAction)

  val registerPane = JPanel( new BorderLayout() )
  registerPane.setLayout( BoxLayout(registerPane, BoxLayout.PAGE_AXIS) )
  registerPane.add(registerForm)
  registerPane.add(registerActions)

  val tabbedPane = JTabbedPane()
  tabbedPane.addTab(Context.login, loginPane)
  tabbedPane.addTab(Context.register, registerPane)

  add(tabbedPane, BorderLayout.CENTER)