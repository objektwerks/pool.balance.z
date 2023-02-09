package objektwerks.pane

import javax.swing.JTabbedPane

import objektwerks.Context

final class ChildrenTabbedPane extends JTabbedPane:
  addTab( Context.cleanings, CleaningsPane() )
  addTab( Context.measurements, MeasurementsPane() )
  addTab( Context.chemicals, ChemicalsPane() )