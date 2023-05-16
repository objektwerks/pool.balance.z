package objektwerks

import com.formdev.flatlaf.FlatDarculaLaf
import com.typesafe.scalalogging.LazyLogging

import java.awt.{EventQueue, Taskbar}
import javax.swing.UIManager

import Context.*
import objektwerks.dialog.LoginRegisterDialog

object Client extends LazyLogging:
  def main(args: Array[String]): Unit =
    Context.init
    EventQueue.invokeLater(
      () => {
        UIManager.setLookAndFeel( FlatDarculaLaf() )
        Taskbar.getTaskbar.setIconImage(logo)
        LoginRegisterDialog().open()
        Frame(logo, title, width, height).setVisible(true)
      }
    )
    logger.info("*** Client running ...")
    sys.addShutdownHook {
      logger.info("*** Client shutdown.")
    }