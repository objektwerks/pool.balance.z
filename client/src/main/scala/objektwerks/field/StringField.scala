package objektwerks.field

import java.awt.Dimension
import java.beans.PropertyChangeEvent
import javax.swing.JTextField

final class StringField(value: String,
                        columns: Int,
                        fireChangeAction: String => Unit) extends JTextField(value):
  setPreferredSize( Dimension( 100, 40) )
  setText(value)
  setColumns(columns)

  addPropertyChangeListener(
    (_: PropertyChangeEvent) => fireChangeAction(getText().trim)
  )