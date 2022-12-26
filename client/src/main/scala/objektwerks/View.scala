package objektwerks

import javax.swing.{BorderFactory, JSplitPane}

import objektwerks.dashboard.DashboardPane
import objektwerks.pane.SplitPane

final class View extends JSplitPane:
  setOrientation(JSplitPane.VERTICAL_SPLIT)
  setOneTouchExpandable(false)
  setDividerLocation(260)
  setTopComponent( DashboardPane() )
  setBottomComponent( SplitPane() )
  setBorder( BorderFactory.createEmptyBorder(3, 3, 3, 3) )