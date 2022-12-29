package objektwerks.dialog

import objektwerks.{Cleaning, Context}
import objektwerks.action.{Actions, CancelAction, SaveCleaningAction}

final class CleaningDialog(cleaning: Cleaning) extends Dialog(Context.cleaning):
  var editedCleaning = cleaning.copy()

  val cancelAction = CancelAction(Context.cancel, () => close())
  val saveAction = SaveCleaningAction(Context.save, editedCleaning)
  val actions = Actions(cancelAction, saveAction)