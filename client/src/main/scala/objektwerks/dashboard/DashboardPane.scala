package objektwerks.dashboard

import javax.swing.{BoxLayout, JPanel}

final class DashboardPane extends JPanel:
  val topPanel = new JPanel()
  topPanel.setLayout( new BoxLayout(topPanel, BoxLayout.LINE_AXIS) )
  topPanel.add( TotalChlorinePane() )
  topPanel.add( FreeChlorinePane() )
  topPanel.add( CombinedChlorinePane() )
  topPanel.add( CalciumHardnessPane() )
  topPanel.add( TotalAlkalinityPane() )

  val bottomPanel = new JPanel()
  bottomPanel.setLayout( new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS) )
  bottomPanel.add( CyanuricAcidPane() )
  bottomPanel.add( TotalBrominePane() )
  bottomPanel.add( PhPane() )
  bottomPanel.add( SaltPane() )
  bottomPanel.add( TemperaturePane() )

  setLayout( new BoxLayout(this, BoxLayout.PAGE_AXIS) )

  add( topPanel )
  add( bottomPanel )