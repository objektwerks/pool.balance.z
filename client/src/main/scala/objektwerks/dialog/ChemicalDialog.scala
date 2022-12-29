package objektwerks.dialog

import javax.swing.{JComponent, JLabel}

import objektwerks.{Context, Chemical, TypeOfChemical, UnitOfMeasure}
import objektwerks.Context.asLabel
import objektwerks.action.{Actions, CancelAction, SaveChemicalAction}
import objektwerks.field.{DoubleField, SelectField}
import objektwerks.form.Form

final class ChemicalDialog(chemical: Chemical) extends Dialog(Context.chemical):
  var editedChemical = chemical.copy()

  val typeof = SelectField(
    TypeOfChemical.toList,
    (value: String) => editedChemical = chemical.copy(typeof = value),
    TypeOfChemical.selectedIndex(chemical.typeof)
  )
  val amount = DoubleField(
    chemical.amount,
    (value: Double) => editedChemical = chemical.copy(amount = value)
  )
  val unit = SelectField(
    UnitOfMeasure.toList,
    (value: String) => editedChemical = chemical.copy(unit = value),
    UnitOfMeasure.selectedIndex(UnitOfMeasure.toList, chemical.unit)
  )

  val form = Form(
    List[(String, JComponent)](
      Context.typeof.asLabel -> typeof,
      Context.amount.asLabel -> amount,
      Context.unit.asLabel -> unit,
      Context.added.asLabel -> new JLabel(chemical.added)
    )
  )

  val cancelAction = CancelAction(Context.cancel, () => close())
  val saveAction = SaveChemicalAction(Context.save, editedChemical)
  val actions = Actions(cancelAction, saveAction)

  add(form, actions)