package objektwerks

import java.awt.BorderLayout
import javax.swing.JPanel

import objektwerks.dashboard.DashboardPane
import objektwerks.pane.SplitPane

final class View extends JPanel:
  setLayout( new BorderLayout() )
  add( DashboardPane(), BorderLayout.NORTH )
  add( SplitPane(), BorderLayout.CENTER )