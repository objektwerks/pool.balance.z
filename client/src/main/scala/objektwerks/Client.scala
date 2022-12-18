package objektwerks

import com.typesafe.scalalogging.LazyLogging

import java.awt.EventQueue
import javax.swing.UIManager

object Client extends LazyLogging:
  def main(args: Array[String]): Unit =
    EventQueue.invokeLater( () => {
      val conf = Resources.loadConfig("client.conf")
      val name = conf.getString("name")
      val width = conf.getInt("width")
      val height = conf.getInt("height")

      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
      val frame = new Frame(name, width, height)
      frame.setVisible(true)
      
      logger.info("Client running!")
    } )