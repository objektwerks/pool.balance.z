package objektwerks

import com.typesafe.scalalogging.LazyLogging

import java.awt.EventQueue
import javax.swing.UIManager.*

object Client extends LazyLogging:
  def main(args: Array[String]): Unit =
    EventQueue.invokeLater( () => {
      setLookAndFeel(getCrossPlatformLookAndFeelClassName)
      val frame = new Frame("Pool Balance", 600, 600)
      frame.setVisible(true)
      logger.info("Started client!")
    })