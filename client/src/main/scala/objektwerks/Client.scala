package objektwerks

import com.typesafe.scalalogging.LazyLogging

import java.awt.EventQueue
import javax.swing.{JFrame, UIManager, WindowConstants}

import Context.*

object Client extends LazyLogging:
  def main(args: Array[String]): Unit =
    EventQueue.invokeLater(
      () => {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        val frame = new JFrame()
        frame.setName(title)
        frame.setTitle(title)
        frame.setIconImage(logoImage)
        frame.setSize(width, height)
        frame.setLocationRelativeTo(null)
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
        frame.setVisible(true)
      }
    )
    logger.info("*** Client running ...")