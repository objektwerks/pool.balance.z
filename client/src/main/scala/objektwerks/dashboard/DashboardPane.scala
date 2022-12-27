package objektwerks.dashboard

import javax.swing.{BoxLayout, JPanel}

final class DashboardPane extends JPanel:
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