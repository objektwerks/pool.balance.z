package objektwerks

import com.typesafe.scalalogging.LazyLogging

import java.awt.EventQueue
import javax.swing.UIManager

object Client extends LazyLogging:
  def main(args: Array[String]): Unit =
    EventQueue.invokeLater( () => {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
      val conf = Resources.loadConfig("client.conf")
      val name = conf.getString("name")
      val frame = new Frame(name, 600, 600)
      frame.setVisible(true)
      logger.info("Client running!")
    } )