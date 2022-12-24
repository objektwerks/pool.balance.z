package objektwerks.pane

import javax.swing.JSplitPane

final class SplitPane extends JSplitPane:
  setLeftComponent( PoolsTabbedPane() )
  setRightComponent( TabbedPane() )
  setOneTouchExpandable(false)
  setDividerLocation(300)