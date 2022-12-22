package objektwerks

import javax.swing.JSplitPane

import objektwerks.dashboard.DashboardPane
import objektwerks.pane.SplitPane

final class View extends JSplitPane:
  setTopComponent( DashboardPane() )
  setBottomComponent( SplitPane() )
  setOrientation(JSplitPane.VERTICAL_SPLIT)
  setOneTouchExpandable(false)
  setDividerLocation(200)