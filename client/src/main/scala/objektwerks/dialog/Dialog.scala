package objektwerks

import java.awt.{BorderLayout, Component}
import javax.swing.JDialog

final class Dialog(title: String,
                   form: Form,
                   actions: Actions,
                   location: Component = null) extends JDialog:
  setTitle(title)
  setLayout( new BorderLayout() )
  setLocationRelativeTo(location)
  setModal(true)
  
  add(form, BorderLayout.CENTER)
  add(actions, BorderLayout.SOUTH)

  def view(isVisible: Boolean): Unit = setVisible(isVisible)