package objektwerks

import com.typesafe.scalalogging.LazyLogging

import java.awt.{EventQueue, Taskbar}
import javax.swing.UIManager

import Context.*

object Client extends LazyLogging:
  def main(args: Array[String]): Unit =
    EventQueue.invokeLater(
      () => {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

        Taskbar.getTaskbar().setIconImage(logo)

        val frame = new Frame(logo, title, width, height)
        frame.setVisible(true)
      }
    )
    logger.info("*** Client running ...")