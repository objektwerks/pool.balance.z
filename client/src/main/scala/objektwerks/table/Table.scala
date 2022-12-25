package objektwerks.table

import javax.swing.{JTable, ListSelectionModel}
import javax.swing.table.{DefaultTableModel, DefaultTableColumnModel, TableColumn}
import javax.swing.event.ListSelectionEvent

private class TableModel[E](rows: List[E]) extends DefaultTableModel:
  addRow( rows.toArray[Any] )

private class ColumnModel(columns: List[String]) extends DefaultTableColumnModel:
  for ((column, index) <- columns.view.zipWithIndex)
    val tableColumn = new TableColumn(index)
    tableColumn.setHeaderValue(column)
    addColumn(tableColumn)

final class Table[E](rows: List[E], columns: List[String]) extends JTable:
  setModel( TableModel(rows) )
  setColumnModel( ColumnModel(columns) )
  setSelectionMode(ListSelectionModel.SINGLE_SELECTION)

  def getId(event: ListSelectionEvent): Option[Long] =
    if !event.getValueIsAdjusting() && getSelectedRow() != -1 then
      val row = convertRowIndexToModel(getSelectedRow())
      val column = 0
      val id = getModel().getValueAt(row, column).toString().toLong
      Some(id)
    else None