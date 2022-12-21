package objektwerks

import java.awt.{GridLayout, Image}
import javax.swing.{JButton, JDialog}

object Dialog:
  val rows = 2
  val columns = 1
  val horizontalGap = 6
  val verticalGap = 6

open class Dialog(image: Image,
                  title: String,
                  form: Form,
                  buttons: JButton*) extends JDialog:
  import Dialog.*

  setIconImage(image)
  setTitle(title)
  setLayout( new GridLayout(rows, columns, horizontalGap, verticalGap) )
  setModal(true)