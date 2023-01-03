package objektwerks.dialog

import javax.swing.JLabel

import objektwerks.{Account, Context}
import objektwerks.form.Form

final class AccountDialog(account: Account) extends Dialog(Context.account):
  val form = Form(
    List[(String, JLabel)](
      "License:" -> JLabel(account.license),
      "Pin:" -> JLabel(account.pin),
      "Activated:" -> JLabel(account.activated),
      "Deactivated:" -> JLabel(account.deactivated)
    )
  )