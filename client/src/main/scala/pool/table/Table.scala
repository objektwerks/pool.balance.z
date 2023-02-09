package objektwerks.table

import java.awt.Dimension
import java.awt.event.{MouseAdapter, MouseEvent}
import javax.swing.{JTable, ListSelectionModel}
import javax.swing.table.{DefaultTableModel, DefaultTableColumnModel, TableColumn, TableColumnModel}
import javax.swing.event.{ListSelectionEvent, ListSelectionListener}

import objektwerks.Entity

final class TableModel(entities: List[Entity]) extends DefaultTableModel:
  entities.foreach { entity => addRow( entity.toArray ) }

final class ColumnModel(columns: List[String]) extends DefaultTableColumnModel:
  for ((column, index) <- columns.view.zipWithIndex)
    val tableColumn = TableColumn(index)
    tableColumn.setHeaderValue(column)
    addColumn(tableColumn)

final class Table(tableModel: TableModel,
                  columnModel: TableColumnModel,
                  setSelectedId: Long => Unit,
                  fireEditActionById: Long => Unit) extends JTable(tableModel, columnModel):
  setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
  getTableHeader.setPreferredSize( Dimension(80, 40) )
  
  getSelectionModel.addListSelectionListener(
    (event: ListSelectionEvent) => getId(event).fold(())(_ => setSelectedId)
  )

  addMouseListener(
    new MouseAdapter {
      override def mouseClicked(event: MouseEvent): Unit = getId(event).fold(())(_ => fireEditActionById )
    }
  )

  private def getId(event: ListSelectionEvent): Option[Long] =
    if !event.getValueIsAdjusting && getSelectedRow != -1 then
      Some( toId( getSelectedRow ) )
    else None

  private def getId(event: MouseEvent): Option[Long] =
    if event.getClickCount == 2 && !getSelectionModel.getValueIsAdjusting && getSelectedRow != -1 then
      Some( toId( getSelectedRow ) )
    else None

  private def toId(selectedRow: Int): Long =
    val row = convertRowIndexToModel(selectedRow)
    val column = 0
    getModel
      .getValueAt(row, column)
      .toString
      .toLong