package objektwerks.dialog

import javax.swing.{JComboBox, JComponent, JFormattedTextField, JTextField}

import objektwerks.{Context, Pool}
import objektwerks.action.{Actions, SavePoolAction}
import objektwerks.form.Form

final class PoolDialog(title: String = "Pool",
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

  val saveAction = SavePoolAction(Context.edit)
  val actions = Actions(saveAction)

  add(form, actions)