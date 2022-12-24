package objektwerks.dashboard

import java.awt.GridLayout
import javax.swing.JPanel

object DashboardPane:
  val rows = 1
  val columns = 9
  val horizontalGap = 6
  val verticalGap = 6

final class DashboardPane extends JPanel:
  import DashboardPane.*

  setLayout( new GridLayout(rows, columns, horizontalGap, verticalGap) )

  add( TotalChlorinePane() )
  add( FreeChlorinePane() )
  add( CombinedChlorinePane() )
  add( PhPane() )
  add( CalciumHardnessPane() )