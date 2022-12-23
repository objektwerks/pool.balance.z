package objektwerks.dashboard

import java.awt.GridLayout
import javax.swing.{JLabel, JPanel}
import javax.swing.border.TitledBorder

abstract class DashboardTitledPane extends JPanel:
  val rows = 1
  val columns = 1
  val horizontalGap = 6
  val verticalGap = 6

  setLayout( new GridLayout(rows, columns, horizontalGap, verticalGap) )
  setBorder( new TitledBorder("<html>Total<br>Chlorine") )

  val range = new JLabel("")
  val good = new JLabel("")
  val ideal = new JLabel("")
  val current = new JLabel("0")
  val average = new JLabel("0")