package objektwerks.dashboard

import com.typesafe.scalalogging.LazyLogging

import java.awt.{Color, GridLayout}
import javax.swing.{BorderFactory, JLabel, JPanel}
import javax.swing.border.TitledBorder

import objektwerks.Form

object DashboardTitledPane:
  val rows = 1
  val columns = 1
  val horizontalGap = 6
  val verticalGap = 6

abstract class DashboardTitledPane(title: String) extends JPanel with LazyLogging:
  import DashboardTitledPane.*

  setLayout( new GridLayout(rows, columns, horizontalGap, verticalGap) )
  setBorder( new TitledBorder(title) )

  val range = new JLabel("")
  val good = new JLabel("")
  val ideal = new JLabel("")
  val current = new JLabel("0")
  val average = new JLabel("0")

  val form = Form(
    List[(String, JLabel)](
      "Range:" -> range,
      "Good:" -> good,
      "Ideal:" -> ideal,
      "Current:" -> current,
      "Average:" -> average
    )
  )
  

  val emptyBorder = new JLabel().getBorder()
  val greenBorder = BorderFactory.createLineBorder(Color.green, 3)
  val redBorder = BorderFactory.createLineBorder(Color.red, 3)

  def inRangeCurrent: Unit =
    good.setBorder(emptyBorder)
    current.setBorder(emptyBorder)

  def outOfRangeCurrent: Unit =
    logger.info(s"DashboardTitledPane.outOfRangeCurrent: ${current.getText()}")
    good.setBorder(greenBorder)
    current.setBorder(redBorder)

  def inRangeAverage: Unit =
    good.setBorder(emptyBorder)
    average.setBorder(emptyBorder)

  def outOfRangeAverage: Unit =
    logger.info(s"DashboardTitledPane.outOfRangeAverage: ${average.getText()}")
    good.setBorder(greenBorder)
    average.setBorder(redBorder)