package objektwerks.dialog

import javax.swing.{JComponent, JLabel}

import objektwerks.{Cleaning, Context}
import objektwerks.Context.asLabel
import objektwerks.action.{Actions, CancelAction, SaveCleaningAction}
import objektwerks.field.BooleanField
import objektwerks.form.Form

final class CleaningDialog(cleaning: Cleaning) extends Dialog(Context.cleaning):
  var editedCleaning = cleaning.copy()

  val brush = BooleanField(
    cleaning.brush,
    (state: Boolean) => editedCleaning = cleaning.copy(brush = state)
  )
  val net = BooleanField(
    cleaning.net,
    (state: Boolean) => editedCleaning = cleaning.copy(net = state)
  )
  val skimmerBasket = BooleanField(
    cleaning.skimmerBasket,
    (state: Boolean) => editedCleaning = cleaning.copy(skimmerBasket = state)
  )
  val pumpBasket = BooleanField(
    cleaning.pumpBasket,
    (state: Boolean) => editedCleaning = cleaning.copy(pumpBasket = state)
  )
  val pumpFilter = BooleanField(
    cleaning.pumpFilter,
    (state: Boolean) => editedCleaning = cleaning.copy(pumpFilter = state)
  )
  val vacuum = BooleanField(
    cleaning.vacuum,
    (state: Boolean) => editedCleaning = cleaning.copy(vacuum = state)
  )

  val form = Form(
    List[(String, JComponent)](
      Context.brush.asLabel -> brush,
      Context.net.asLabel -> net,
      Context.skimmerBasket.asLabel -> skimmerBasket,
      Context.pumpBasket.asLabel -> pumpBasket,
      Context.pumpFilter.asLabel -> pumpFilter,
      Context.vacuum.asLabel -> vacuum,
      Context.cleaned.asLabel -> JLabel(cleaning.cleaned)
    )
  )

  val cancelAction = CancelAction(Context.cancel, () => close())
  val saveAction = SaveCleaningAction(Context.save, editedCleaning)
  val actions = Actions(cancelAction, saveAction)

  add(form, actions)