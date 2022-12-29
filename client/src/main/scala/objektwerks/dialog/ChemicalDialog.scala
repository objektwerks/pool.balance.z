package objektwerks.dialog

import javax.swing.{JComponent, JLabel}

import objektwerks.{Context, Chemical, TypeOfChemical}
import objektwerks.action.{Actions, CancelAction, SaveChemicalAction}
import objektwerks.field.{DoubleField, SelectField}
import objektwerks.form.Form

final class ChemicalDialog(chemical: Chemical) extends Dialog(Context.chemical):
  var editedChemical = chemical.copy()

  val typeof = SelectField( TypeOfChemical.toList, (value: String) => editedChemical = chemical.copy(typeof = value) )


  val cancelAction = CancelAction(Context.cancel, () => close())
  val saveAction = SaveChemicalAction(Context.save, editedChemical)
  val actions = Actions(cancelAction, saveAction)