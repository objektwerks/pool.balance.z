package objektwerks.dashboard

import com.typesafe.scalalogging.LazyLogging

import java.awt.{Color, GridLayout}
import javax.swing.{BorderFactory, JLabel, JPanel}
import javax.swing.border.TitledBorder

import objektwerks.form.Form

object DashboardTitledPane:
  val rows = 1
  val columns = 1
  val horizontalGap = 6
  val verticalGap = 6

  val fuchsia = Color(255, 0, 255)
  val emptyBorder = new JLabel().getBorder
  val greenBorder = BorderFactory.createLineBorder(Color.green, 3)
  val redBorder = BorderFactory.createLineBorder(Color.red, 3)

abstract class DashboardTitledPane(title: String) extends JPanel with LazyLogging:
  import DashboardTitledPane.*

  setLayout( new GridLayout(rows, columns, horizontalGap, verticalGap) )

  val titleBorder = new TitledBorder(title)
  titleBorder.setTitleColor(fuchsia)
  setBorder(titleBorder)

  val range = new JLabel("")
  val good = new JLabel("")
  val ideal = new JLabel("")
  val current = new JLabel("0")
  val average = new JLabel("0")

  private val form = Form(
    List[(String, JLabel)](
      "Range:" -> range,
      "Good:" -> good,
      "Ideal:" -> ideal,
      "Last:" -> current,
      "Avg:" -> average
    )
  )
  add(form)

  def currentIsInRange(): Unit =
    good.setBorder(emptyBorder)
    current.setBorder(emptyBorder)

  def currentIsOutOfRange(): Unit =
    logger.info(s"DashboardTitledPane.currentIsOutOfRange: ${current.getText}")
    good.setBorder(greenBorder)
    current.setBorder(redBorder)

  def averageIsInRange(): Unit =
    good.setBorder(emptyBorder)
    average.setBorder(emptyBorder)

  def averageIsOutOfRange(): Unit =
    logger.info(s"DashboardTitledPane.averageIsOutOfRange: ${average.getText}")
    good.setBorder(greenBorder)
    average.setBorder(redBorder)