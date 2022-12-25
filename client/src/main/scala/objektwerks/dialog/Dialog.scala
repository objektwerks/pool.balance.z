package objektwerks

import java.awt.{Component, GridLayout, Image}
import javax.swing.{JDialog, JPanel}

object Dialog:
  val rows = 2
  val columns = 1
  val horizontalGap = 6
  val verticalGap = 6

final class Dialog(title: String,
                   form: Form,
                   actions: Actions,
                   location: Component = null) extends JDialog:
  import Dialog.*

  setTitle(title)
  setLayout( new GridLayout(rows, columns, horizontalGap, verticalGap) )
  setLocationRelativeTo(location)
  setModal(true)
  add(form)
  add(actions)

  def view(isVisible: Boolean): Unit = setVisible(isVisible)