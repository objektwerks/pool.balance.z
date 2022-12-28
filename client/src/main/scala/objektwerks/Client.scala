package objektwerks

import com.formdev.flatlaf.FlatDarculaLaf
import com.typesafe.scalalogging.LazyLogging

import java.awt.{EventQueue, Taskbar}
import javax.swing.UIManager

import Context.*

object Client extends LazyLogging:
  def main(args: Array[String]): Unit =
    EventQueue.invokeLater(
      () => {
        System.setProperty("apple.awt.application.name", Context.title)
        UIManager.setLookAndFeel( new FlatDarculaLaf() )
        Taskbar.getTaskbar.setIconImage(logo)
        Frame(logo, title, width, height).setVisible(true)
      }
    )
    logger.info("*** Client running ...")