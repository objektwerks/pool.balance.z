package objektwerks

import javax.swing.JTable
import javax.swing.table.{ DefaultTableModel, DefaultTableColumnModel, TableColumn }

final class TableModel[E](rows: List[E]) extends DefaultTableModel:
  addRow( rows.toArray[Any] )

final class ColumnModel(columns: List[String]) extends DefaultTableColumnModel:
  for ((column, index) <- columns.view.zipWithIndex)
    val tableColumn = new TableColumn(index)
    tableColumn.setHeaderValue(column)
    addColumn(tableColumn)

final class Table[E](rows: List[E], columns: List[String]) extends JTable:
  setModel( TableModel(rows) )
  setColumnModel( ColumnModel(columns) )