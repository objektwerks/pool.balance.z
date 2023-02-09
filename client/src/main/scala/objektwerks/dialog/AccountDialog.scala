package objektwerks.dialog

import java.time.LocalDate
import javax.swing.JLabel

import objektwerks.{Account, Context}
import objektwerks.action.{Actions, DeactivateAccountAction, ReactivateAccountAction, CloseAction}
import objektwerks.form.Form

final class AccountDialog(account: Account) extends Dialog(Context.account):
  val form = Form(
    List[(String, JLabel)](
      Context.license -> JLabel(account.license),
      Context.pin -> JLabel(account.pin),
      Context.activated -> JLabel( LocalDate.ofEpochDay(account.activated).toString ),
      Context.deactivated -> JLabel( LocalDate.ofEpochDay(account.deactivated).toString )
    )
  )

  val closeAction = CloseAction(Context.close, () => close())
  val deactivateAction = DeactivateAccountAction(Context.deactivated)
  val reactivateAction = ReactivateAccountAction(Context.activated)
  val actions = Actions(closeAction, deactivateAction, reactivateAction)

  add(form, actions)