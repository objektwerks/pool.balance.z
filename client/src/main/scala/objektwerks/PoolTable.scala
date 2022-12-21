package objektwerks

import javax.swing.JTable
import javax.swing.table.{ DefaultTableModel, DefaultTableColumnModel, TableColumn }

final class PoolTableModel(pools: List[Pool]) extends DefaultTableModel:
  addRow( pools.toArray[Object] )

final class PoolColumnModel(columns: List[String]) extends DefaultTableColumnModel:
  for ((column, index) <- columns.view.zipWithIndex)
    val tableColumn = new TableColumn(index)
    tableColumn.setHeaderValue(column)
    addColumn(tableColumn)

final class PoolTable(pools: List[Pool], columns: List[String]) extends JTable:
  setModel( PoolTableModel(pools) )
  setColumnModel( PoolColumnModel(columns) )