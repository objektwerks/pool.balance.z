package objektwerks

import javax.swing.JTable
import javax.swing.table.DefaultTableModel

final class PoolTableModel(pools: Array[Pool]) extends DefaultTableModel:
  addRow( pools.toArray[Object] )

final class PoolTable(pools: List[Pool]) extends JTable:
  