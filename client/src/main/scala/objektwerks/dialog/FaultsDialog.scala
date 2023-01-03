package objektwerks.dialog

import javax.swing.JScrollPane

import objektwerks.{Context, Model}
import objektwerks.action.{Actions, CloseAction}
import objektwerks.table.{ColumnModel, Table, TableModel}

final class FaultsDialog extends Dialog(Context.faults):
  val faults = Model.observableFaults.toList
  val columns = List(occurred, cause)
  val table = Table(
    TableModel(faults),
    ColumnModel(columns),
    Long => setSelectedId(Long),
    Long => fireEditActionById(Long)
  )
  Model.observablePools.onChange { (_, _) =>
    table.setModel( TableModel( Model.observablePools.toList ) )
  }
  val tablePane = new JScrollPane(table)

  def setSelectedId(id: Long): Unit = Model.selectedPoolId.value = id

  def fireEditActionById(id: Long): Unit = ()
  
  setLayout( new BorderLayout() )

  val closeAction = CloseAction(Context.close, () => close())
  val actions = Actions(closeAction)

  add(tablePane, actions)