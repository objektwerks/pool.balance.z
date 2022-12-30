package objektwerks.pane

import java.awt.BorderLayout
import javax.swing.JPanel

import objektwerks.Model
import objektwerks.Context.*

final class CleaningsPane extends JPanel:
  val cleanings = Model.observableCleanings.toList
  val columns = List(id, poolId, brush, net, skimmerBasket, pumpBasket, pumpFilter, vacuum, cleaned)

  setLayout( new BorderLayout() )