package objektwerks.table

import javax.swing.{JTable, ListSelectionModel}
import javax.swing.table.{DefaultTableModel, DefaultTableColumnModel, TableColumn}
import javax.swing.event.ListSelectionEvent

import objektwerks.Entity

private class ColumnModel(columns: List[String]) extends DefaultTableColumnModel:
  for ((column, index) <- columns.view.zipWithIndex)
    val tableColumn = new TableColumn(index)
    tableColumn.setHeaderValue(column)
    addColumn(tableColumn)

private class TableModel(entities: List[Entity]) extends DefaultTableModel:
  entities.foreach { entity => addRow( entity.toArray ) }

final class Table(columns: List[String], entities: List[Entity]) extends JTable:
  setColumnModel( ColumnModel(columns) )
  setModel( TableModel(entities) )
  setSelectionMode(ListSelectionModel.SINGLE_SELECTION)

  def getId(event: ListSelectionEvent): Option[Long] =
    if !event.getValueIsAdjusting() && getSelectedRow() != -1 then
      val row = convertRowIndexToModel(getSelectedRow())
      val column = 0
      val id = getModel().getValueAt(row, column).toString().toLong
      Some(id)
    else None