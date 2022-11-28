package objektwerks

import java.awt.EventQueue

import javax.swing.UIManager._

object Client:
  def main(args: Array[String]): Unit =
    EventQueue.invokeLater( () => {
      setLookAndFeel(getCrossPlatformLookAndFeelClassName)
      val frame = new Frame()
      frame.setVisible(true)
    })