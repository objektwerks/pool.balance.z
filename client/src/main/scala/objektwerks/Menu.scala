package objektwerks

import javax.swing.{JMenu, JMenuBar, JMenuItem}

import objektwerks.Context.*
import objektwerks.action.ChartAction
import objektwerks.chart.Chart.*

final class Menu() extends JMenuBar:
  val charts = JMenu("Charts")
  charts.add( JMenuItem( ChartAction(totalChlorine, buildTotalChlorineChart) ) )
  charts.add( JMenuItem( ChartAction(freeChlorine, buildFreeChlorineChart) ) )
  charts.add( JMenuItem( ChartAction(combinedChlorine, buildCombinedChlorineChart) ) )
  charts.add( JMenuItem( ChartAction(ph, buildPhChart) ) )
  charts.add( JMenuItem( ChartAction(calciumHardness, buildCalciumHardnessChart) ) )
  charts.add( JMenuItem( ChartAction(totalAlkalinity, buildTotalAlkalinityChart) ) )
  charts.add( JMenuItem( ChartAction(cyanuricAcid, buildCyanuricAcidChart) ) )
  charts.add( JMenuItem( ChartAction(totalBromine, buildTotalBromineChart) ) )
  charts.add( JMenuItem( ChartAction(salt, buildSaltCharrt) ) )
  charts.add( JMenuItem( ChartAction(temperature, buildTemperatureChart) ) )
  add(charts)