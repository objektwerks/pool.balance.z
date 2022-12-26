package objektwerks.dialog

import javax.swing.{JComboBox, JComponent, JFormattedTextField, JTextField}

import objektwerks.{Context, Pool}
import objektwerks.action.{Actions, CancelAction, SavePoolAction}
import objektwerks.form.Form

final class PoolDialog(title: String = Context.pool,
                       pool: Pool) extends Dialog(title):
  val name = new JTextField()
  val volume = new JFormattedTextField()
  val unit = new JComboBox()

  val form = Form(
    List[(String, JComponent)](
      "Name:" -> name,
      "Volume:" -> volume,
      "Unit:" -> unit
    )
  )

  val cancelAction = CancelAction(Context.cancel, () => view(false))
  val saveAction = SavePoolAction(Context.edit, pool)
  val actions = Actions(cancelAction, saveAction)

  add(form, actions)