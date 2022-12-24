package objektwerks.dashboard

import java.awt.GridLayout
import javax.swing.{BoxLayout, JPanel}

object DashboardPane:
  val rows = 1
  val columns = 9
  val horizontalGap = 6
  val verticalGap = 6

final class DashboardPane extends JPanel:
  import DashboardPane.*

  setLayout( new BoxLayout(this, BoxLayout.LINE_AXIS) )

  add( TotalChlorinePane() )
  add( FreeChlorinePane() )
  add( CombinedChlorinePane() )
  add( PhPane() )
  add( CalciumHardnessPane() )
  add( TotalAlkalinityPane() )
  add( CyanuricAcidPane() )
  add( TotalBrominePane() )
  add( SaltPane() )
  add( TemperaturePane() )