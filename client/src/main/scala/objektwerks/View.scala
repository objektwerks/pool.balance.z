package objektwerks

import java.awt.GridLayout
import javax.swing.JPanel

import objektwerks.dashboard.DashboardPane
import objektwerks.pane.SplitPane

object View:
  val rows = 2
  val columns = 1
  val horizontalGap = 6
  val verticalGap = 6

final class View extends JPanel:
  import View.*

  setLayout( new GridLayout(rows, columns, horizontalGap, verticalGap) )

  add( DashboardPane() )
  add( SplitPane() )