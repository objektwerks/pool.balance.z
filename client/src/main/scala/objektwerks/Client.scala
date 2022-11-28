package objektwerks

import java.awt.EventQueue

import javax.swing.JFrame
import javax.swing.UIManager._

object Client:
  def main(args: Array[String]): Unit =
    EventQueue.invokeLater( new Runnable() {
      override def run(): Unit = {
        setLookAndFeel(getCrossPlatformLookAndFeelClassName)
        val frame = new JFrame()
        frame.setVisible(true)
      }
    })