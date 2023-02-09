package objektwerks.pane

import javax.swing.JTabbedPane

import objektwerks.Context

final class PoolsTabbedPane extends JTabbedPane:
  addTab( Context.pools, PoolsPane() )