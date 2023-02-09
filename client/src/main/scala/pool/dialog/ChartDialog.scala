package objektwerks.dialog

import org.jfree.chart.ChartPanel

import objektwerks.Context
import objektwerks.action.{Actions, CloseAction}

class ChartDialog(title: String, chart: ChartPanel) extends Dialog(title):
  val closeAction = CloseAction(Context.close, () => close())
  val actions = Actions(closeAction)

  add(chart, actions)