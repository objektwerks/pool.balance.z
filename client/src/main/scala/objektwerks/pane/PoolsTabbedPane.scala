package objektwerks.pane

import javax.swing.{BorderFactory, JTabbedPane}

final class PoolsTabbedPane extends JTabbedPane:
  setBorder( BorderFactory.createEmptyBorder(3, 3, 3, 3) )

  addTab( "Pools", PoolsPane() )