package objektwerks

import javax.swing.{JMenu, JMenuBar, JMenuItem}

import objektwerks.Context.*
import objektwerks.action.ChartAction
import objektwerks.chart.Chart.*

final class Menu(frame: Frame) extends JMenuBar:
  val charts = new JMenu("Charts")
  charts.add( new JMenuItem( ChartAction(totalChlorine, buildTotalChlorineChart) ) )
  charts.add( new JMenuItem( ChartAction(freeChlorine, buildFreeChlorineChart) ) )
  charts.add( new JMenuItem( ChartAction(combinedChlorine, buildCombinedChlorineChart) ) )
  charts.add( new JMenuItem( ChartAction(ph, buildPhChart) ) )
  charts.add( new JMenuItem( ChartAction(calciumHardness, buildCalciumHardnessChart) ) )
  charts.add( new JMenuItem( ChartAction(totalAlkalinity, buildTotalAlkalinityChart) ) )
  charts.add( new JMenuItem( ChartAction(cyanuricAcid, buildCyanuricAcidChart) ) )
  charts.add( new JMenuItem( ChartAction(totalBromine, buildTotalBromineChart) ) )
  charts.add( new JMenuItem( ChartAction(salt, buildSaltCharrt) ) )
  charts.add( new JMenuItem( ChartAction(temperature, buildTemperatureChart) ) )
  add(charts)