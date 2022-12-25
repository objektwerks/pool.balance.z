package objektwerks.table

import javax.swing.{JTable, ListSelectionModel}
import javax.swing.table.{DefaultTableModel, DefaultTableColumnModel, TableColumn}
import javax.swing.event.ListSelectionEvent

import objektwerks.Entity
import javax.swing.table.TableColumnModel

final class TableModel(entities: List[Entity]) extends DefaultTableModel:
  entities.foreach { entity => addRow( entity.toArray ) }

final class ColumnModel(columns: List[String]) extends DefaultTableColumnModel:
  for ((column, index) <- columns.view.zipWithIndex)
    val tableColumn = new TableColumn(index)
    tableColumn.setHeaderValue(column)
    addColumn(tableColumn)

final class Table(tableModel: TableModel, columnsModel: TableColumnModel) extends JTable(tableModel, columnsModel):
  setSelectionMode(ListSelectionModel.SINGLE_SELECTION)

  def getId(event: ListSelectionEvent): Option[Long] =
    if !event.getValueIsAdjusting() && getSelectedRow() != -1 then
      val row = convertRowIndexToModel(getSelectedRow())
      val column = 0
      val id = getModel().getValueAt(row, column).toString().toLong
      Some(id)
    else None