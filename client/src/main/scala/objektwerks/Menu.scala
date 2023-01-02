package objektwerks

import javax.swing.{JMenu, JMenuBar, JMenuItem, JSeparator}

import objektwerks.Context.*
import objektwerks.action.ChartAction
import objektwerks.chart.Chart.*

class Menu(frame: Frame) extends JMenuBar():
  val charts = new JMenu("Charts")
  charts.add( new JMenuItem( new ChartAction(totalChlorine, buildTotalChlorineChart) ) )
  add(charts)