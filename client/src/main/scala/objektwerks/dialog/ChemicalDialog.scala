package objektwerks.dialog

import objektwerks.{Context, Chemical}
import objektwerks.action.{Actions, CancelAction, SaveChemicalAction}

final class ChemicalDialog(chemical: Chemical) extends Dialog(Context.chemical):
  var editedChemical = chemical.copy()

  val cancelAction = CancelAction(Context.cancel, () => close())
  val saveAction = SaveChemicalAction(Context.save, editedChemical)
  val actions = Actions(cancelAction, saveAction)