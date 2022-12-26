package objektwerks.dialog

import javax.swing.JComponent

import objektwerks.{Context, Pool, UnitOfMeasure}
import objektwerks.action.{Actions, CancelAction, SavePoolAction}
import objektwerks.field.{IntField, SelectField, StringField}
import objektwerks.form.Form

final class PoolDialog(pool: Pool) extends Dialog(Context.pool):
  val name = new StringField( pool.name, 24 )
  val volume = IntField( pool.volume )
  val unit = SelectField( UnitOfMeasure.toList )

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