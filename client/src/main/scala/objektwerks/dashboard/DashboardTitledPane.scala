package objektwerks.dashboard

import com.typesafe.scalalogging.LazyLogging

import java.awt.{Color, BorderLayout}
import javax.swing.{BorderFactory, JLabel, JPanel}
import javax.swing.border.TitledBorder

import objektwerks.Context
import objektwerks.form.Form

object DashboardTitledPane:
  val emptyBorder = new JLabel().getBorder
  val greenBorder = BorderFactory.createLineBorder(Color.green, 3)
  val redBorder = BorderFactory.createLineBorder(Color.red, 3)

abstract class DashboardTitledPane(title: String) extends JPanel with LazyLogging:
  import DashboardTitledPane.*

  val titleBorder = new TitledBorder(title)
  titleBorder.setTitleColor(Context.fuchsia)
  setBorder(titleBorder)
  setLayout( new BorderLayout() )

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
  add(form, BorderLayout.CENTER)

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