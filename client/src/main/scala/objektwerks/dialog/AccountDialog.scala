package objektwerks.dialog

import javax.swing.JLabel

import objektwerks.{Account, Context}
import objektwerks.action.{Actions, ActivateAccountAction, DeactivateAccountAction, CloseAction}
import objektwerks.form.Form

final class AccountDialog(account: Account) extends Dialog(Context.account):
  val form = Form(
    List[(String, JLabel)](
      Context.license -> JLabel(account.license),
      Context.pin -> JLabel(account.pin),
      Context.activated -> JLabel(account.activated),
      Context.deactivated -> JLabel(account.deactivated)
    )
  )

  val closeAction = CloseAction(Context.close, () => close())
  val activateAction = ActivateAccountAction(Context.activated)
  val deactivateAction = DeactivateAccountAction(Context.deactivated)
  val actions = Actions(closeAction, activateAction, deactivateAction)

  add(form, actions)