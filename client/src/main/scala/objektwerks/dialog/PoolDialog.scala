package objektwerks.dialog

import javax.swing.{JComboBox, JComponent, JFormattedTextField, JTextField}

import objektwerks.Pool
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