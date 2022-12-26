package objektwerks.pane

import javax.swing.{BorderFactory, JTabbedPane}

final class TabbedPane extends JTabbedPane:
  setBorder( BorderFactory.createEmptyBorder(3, 3, 3, 3) )

  addTab( "Cleanings", CleaningsPane() )
  addTab( "Measurements", MeasurementsPane() )
  addTab( "Chemicals", ChemicalsPane() )