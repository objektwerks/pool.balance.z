package objektwerks.dialog

import javax.swing.JComponent

import objektwerks.{Cleaning, Context}
import objektwerks.action.{Actions, CancelAction, SaveCleaningAction}
import objektwerks.field.BooleanField
import objektwerks.form.Form

final class CleaningDialog(cleaning: Cleaning) extends Dialog(Context.cleaning):
  var editedCleaning = cleaning.copy()

  val brush = BooleanField( cleaning.brush, (state: Boolean) => editedCleaning = cleaning.copy(brush = state) )

  val form = Form(
    List[(String, JComponent)](
      "Brush:" -> brush
    )
  )

  val cancelAction = CancelAction(Context.cancel, () => close())
  val saveAction = SaveCleaningAction(Context.save, editedCleaning)
  val actions = Actions(cancelAction, saveAction)