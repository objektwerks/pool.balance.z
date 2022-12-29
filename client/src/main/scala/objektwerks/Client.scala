package objektwerks

import com.formdev.flatlaf.FlatDarculaLaf
import com.formdev.flatlaf.util.SystemInfo
import com.typesafe.scalalogging.LazyLogging

import java.awt.{EventQueue, Taskbar}
import javax.swing.UIManager

import Context.*

object Client extends LazyLogging:
  def main(args: Array[String]): Unit =
    Context.init
    EventQueue.invokeLater(
      () => {
        UIManager.setLookAndFeel( new FlatDarculaLaf() )
        Taskbar.getTaskbar.setIconImage(logo)
        Frame(logo, title, width, height).setVisible(true)
      }
    )
    logger.info("*** Client running ...")