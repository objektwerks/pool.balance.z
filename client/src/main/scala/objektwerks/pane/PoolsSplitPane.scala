package objektwerks.pane

import javax.swing.JSplitPane

final class PoolsSplitPane extends JSplitPane:
  setLeftComponent( PoolsTabbedPane() )
  setRightComponent( TabbedPane() )
  setOneTouchExpandable(false)
  setDividerLocation(300)