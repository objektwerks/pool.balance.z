package objektwerks

import com.typesafe.scalalogging.LazyLogging

import java.awt.EventQueue
import javax.swing.{JFrame, UIManager}

import Context.*

object Client extends LazyLogging:
  def main(args: Array[String]): Unit =
    EventQueue.invokeLater(
      () => {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        val frame = new JFrame()
        frame.setTitle(title)
        frame.setSize(width, height)
        frame.setIconImage(logoImage)
        frame.setLocationRelativeTo(null)
        frame.setVisible(true)
      }
    )
    logger.info("*** Client running ...")