package objektwerks.dialog

import javax.swing.JComponent

import objektwerks.{Context, Pool, UnitOfMeasure}
import objektwerks.action.{Actions, CancelAction, SavePoolAction}
import objektwerks.field.{IntField, SelectField, StringField}
import objektwerks.form.Form

final class PoolDialog(pool: Pool) extends Dialog(Context.pool):
  var editedPool = pool.copy()

  val name = StringField( pool.name, 24, (value: String) => editedPool = pool.copy(name = value) )
  val volume = IntField( pool.volume, (value: Int) => editedPool = pool.copy(volume = value) )
  val unit = SelectField( UnitOfMeasure.toList, (value: String) => editedPool = pool.copy(unit = value))

  val form = Form(
    List[(String, JComponent)](
      "Name:" -> name,
      "Volume:" -> volume,
      "Unit:" -> unit
    )
  )

  val cancelAction = CancelAction(Context.cancel, () => close())
  val saveAction = SavePoolAction(Context.save, editedPool)
  val actions = Actions(cancelAction, saveAction)

  add(form, actions)