package objektwerks.pane

import java.awt.BorderLayout
import javax.swing.JPanel

import objektwerks.Context
import objektwerks.action.{Actions, AddPoolAction, EditPoolAction}

final class PoolsPane extends JPanel:
  setLayout( new BorderLayout() )

  val addAction = AddPoolAction(Context.add, Context.addImageIcon)
  val editAction = EditPoolAction(Context.edit, Context.editImageIcon)
  val actions = Actions(addAction, editAction)

  // add table
  // add actions