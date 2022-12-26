package objektwerks.pane

import javax.swing.{BorderFactory, JSplitPane}

final class SplitPane extends JSplitPane:
  setLeftComponent( PoolsTabbedPane() )
  setRightComponent( TabbedPane() )
  setOneTouchExpandable(false)
  setDividerLocation(300)
  setBorder( BorderFactory.createEmptyBorder(3, 3, 3, 3) )