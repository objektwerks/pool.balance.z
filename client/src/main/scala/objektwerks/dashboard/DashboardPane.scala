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

/*
  totalChlorineRange = "0 - 10"
  totalChlorineGood = "1 - 5"
  totalChlorineIdeal = "3"

  freeChlorineRange = "0 - 10"
  freeChlorineGood = "1 - 5"
  freeChlorineIdeal = "3"

  combinedChlorineRange = "0 - 0.5"
  combinedChlorineGood = "0.1 - 0.2"
  combinedChlorineIdeal = "0"

  phRange = "6.2 - 8.4"
  phGood = "7.2 - 7.6"
  phIdeal = "7.4"

  calciumHardnessRange = "0 - 1000" 
  calciumHardnessGood = "200 - 500" 
  calciumHardnessIdeal = "375"

  totalAlkalinityRange = "0 - 240"
  totalAlkalinityGood = "80 - 120"
  totalAlkalinityIdeal = "100"

  cyanuricAcidRange = "0 - 300"
  cyanuricAcidGood = "30 - 100"
  cyanuricAcidIdeal = "50"

  totalBromineRange = "0 - 20"
  totalBromineGood = "2 - 10"
  totalBromineIdeal = "5"

  saltRange = "0 - 3600"
  saltGood = "2700 - 3400"
  saltIdeal = "3200"
  
  temperatureRange = "50 - 110"
  temperatureGood = "75 - 85"
  temperatureIdeal = "82"
*/