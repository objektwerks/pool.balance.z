package objektwerks.pane

import javax.swing.JTabbedPane

final class ChildrenTabbedPane extends JTabbedPane:
  addTab( "Cleanings", CleaningsPane() )
  addTab( "Measurements", MeasurementsPane() )
  addTab( "Chemicals", ChemicalsPane() )