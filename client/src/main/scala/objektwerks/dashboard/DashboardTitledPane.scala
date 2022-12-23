package objektwerks.dashboard

import java.awt.GridLayout
import javax.swing.{JLabel, JPanel}
import javax.swing.border.TitledBorder

import objektwerks.Form

abstract class DashboardTitledPane(title: String) extends JPanel:
  private val rows = 1
  private val columns = 1
  private val horizontalGap = 6
  private val verticalGap = 6

  setLayout( new GridLayout(rows, columns, horizontalGap, verticalGap) )
  setBorder( new TitledBorder(title) )

  val range = new JLabel("")
  val good = new JLabel("")
  val ideal = new JLabel("")
  val current = new JLabel("0")
  val average = new JLabel("0")

  val controls = List[(String, JLabel)](
    "Range:" -> range,
    "Good:" -> good,
    "Ideal:" -> ideal,
    "Current:" -> current,
    "Average:" -> average
  )
  
  val form = Form(controls)