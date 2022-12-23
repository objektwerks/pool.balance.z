package objektwerks.dashboard

import com.typesafe.scalalogging.LazyLogging

import java.awt.{Color, GridLayout}
import javax.swing.{BorderFactory, JLabel, JPanel}
import javax.swing.border.TitledBorder

import objektwerks.Form

abstract class DashboardTitledPane(title: String) extends JPanel with LazyLogging:
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

  val emptyBorder = new JLabel().getBorder()
  val greenBorder = BorderFactory.createLineBorder(Color.green)
  val redBorder = BorderFactory.createLineBorder(Color.red)


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