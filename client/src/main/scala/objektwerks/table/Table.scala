package objektwerks

import javax.swing.JTable
import javax.swing.table.{ DefaultTableModel, DefaultTableColumnModel, TableColumn }

final class TableModel[E](entities: List[E]) extends DefaultTableModel:
  addRow( entities.toArray[Any] )

final class ColumnModel(columns: List[String]) extends DefaultTableColumnModel:
  for ((column, index) <- columns.view.zipWithIndex)
    val tableColumn = new TableColumn(index)
    tableColumn.setHeaderValue(column)
    addColumn(tableColumn)

final class Table[E](entities: List[E], columns: List[String]) extends JTable:
  setModel( TableModel(entities) )
  setColumnModel( ColumnModel(columns) )