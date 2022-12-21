package objektwerks

import java.awt.{GridLayout, Image}
import javax.swing.{JPanel, JDialog}

object Dialog:
  val rows = 2
  val columns = 1
  val horizontalGap = 6
  val verticalGap = 6

final class Dialog(frame: Frame,
                   image: Image,
                   title: String,
                   form: Form,
                   actions: Actions) extends JDialog:
  import Dialog.*

  setIconImage(image)
  setTitle(title)
  setLayout( new GridLayout(rows, columns, horizontalGap, verticalGap) )
  setModal(true)
  add(form)
  add(actions)

  def view(isVisible: Boolean): Unit =
    setLocationRelativeTo(frame)
    setVisible(isVisible)