package objektwerks.dialog

import javax.swing.{JComboBox, JComponent, JFormattedTextField, JTextField}

import scala.jdk.CollectionConverters.*

import objektwerks.{Context, Pool, UnitOfMeasure}
import objektwerks.action.{Actions, CancelAction, SavePoolAction}
import objektwerks.field.{IntField, StringField}
import objektwerks.form.Form

final class PoolDialog(title: String = Context.pool,
                       pool: Pool) extends Dialog(title):
  val name = new StringField( pool.name )
  val volume = IntField( pool.volume )
  val unit = new JComboBox( UnitOfMeasure.toList.asJava.toArray() )

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