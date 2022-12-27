package objektwerks

import javax.swing.{BorderFactory, JSplitPane}

import objektwerks.dashboard.DashboardPane
import objektwerks.pane.PoolsSplitPane

final class View extends JSplitPane:
  setOrientation(JSplitPane.VERTICAL_SPLIT)
  setOneTouchExpandable(false)
  setDividerLocation(130)
  setTopComponent( DashboardPane() )
  setBottomComponent( PoolsSplitPane() )
  setBorder( BorderFactory.createEmptyBorder(3, 3, 3, 3) )