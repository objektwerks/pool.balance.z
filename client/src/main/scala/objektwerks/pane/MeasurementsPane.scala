package objektwerks.pane

import java.awt.BorderLayout
import javax.swing.JPanel

import objektwerks.Model

final class MeasurementsPane extends JPanel:
  val measurements = Model.observableMeasurements.toList

  setLayout( new BorderLayout() )