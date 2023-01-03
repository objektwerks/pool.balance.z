package objektwerks.pane

import javax.swing.JSplitPane

final class PoolsSplitPane extends JSplitPane:
  setLeftComponent( PoolsTabbedPane() )
  setRightComponent( ChildrenTabbedPane() )
  setOneTouchExpandable(false)
  setDividerLocation(320)