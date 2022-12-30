package objektwerks.pane

import java.awt.BorderLayout
import javax.swing.JPanel

import objektwerks.Model
import objektwerks.Context.*

final class MeasurementsPane extends JPanel:
  val measurements = Model.observableMeasurements.toList
  val columns = List(
    id, poolId, totalChlorine, freeChlorine, combinedChlorine, ph, calciumHardness,
    totalAlkalinity, cyanuricAcid, totalBromine, salt, temperature, measured
  )

  setLayout( new BorderLayout() )