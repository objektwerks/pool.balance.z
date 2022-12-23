package objektwerks.dashboard

import javax.swing.JLabel

abstract class DashboardTitledPane:
  val range = new JLabel("")
  val good = new JLabel("")
  val ideal = new JLabel("")
  val current = new JLabel("0")
  val average = new JLabel("0")