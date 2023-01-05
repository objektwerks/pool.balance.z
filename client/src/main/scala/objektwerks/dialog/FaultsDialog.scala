package objektwerks.dialog

import java.awt.Dimension

import javax.swing.{JScrollPane, JTable}
import javax.swing.{JTable, ListSelectionModel}
import javax.swing.table.{DefaultTableModel, DefaultTableColumnModel, TableColumn, TableColumnModel}

import objektwerks.{Context, Fault, Model}
import objektwerks.action.{Actions, CloseAction}

final class TableModel(faults: List[Fault]) extends DefaultTableModel:
  faults.foreach { fault => addRow( Array(fault.occurred, fault.cause).toArray[Any] ) }

final class ColumnModel(columns: List[String]) extends DefaultTableColumnModel:
  for ((column, index) <- columns.view.zipWithIndex)
    val tableColumn = TableColumn(index)
    tableColumn.setHeaderValue(column)
    addColumn(tableColumn)

final class FaultsDialog() extends Dialog(Context.faults):
  val faults = Model.observableFaults.toList
  val columns = List(Context.occurred, Context.cause)
  val table = JTable(
    TableModel(faults),
    ColumnModel(columns)
  )

  table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
  table.getTableHeader.setPreferredSize( Dimension(80, 40) )

  Model.observableFaults.onChange { (_, _) =>
    table.setModel( TableModel( Model.observableFaults.toList ) )
  }
  
  val tablePane = JScrollPane(table)
  
  val closeAction = CloseAction(Context.close, () => close())
  val actions = Actions(closeAction)

  add(tablePane, actions)