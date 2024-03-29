package objektwerks.dialog

import java.awt.BorderLayout
import javax.swing.JDialog

import objektwerks.action.Actions
import objektwerks.form.Form

open class Dialog(title: String) extends JDialog:
  setTitle(title)
  setLayout( BorderLayout() )
  setLocationRelativeTo(null)
  setModal(true)

  def add(form: Form, actions: Actions): Unit =
    add(form, BorderLayout.CENTER)
    add(actions, BorderLayout.SOUTH)

  def open(): Unit = setVisible(true)

  def close(): Unit =
    setVisible(false)
    dispose()