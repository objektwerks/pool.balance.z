package objektwerks.field

import java.beans.PropertyChangeEvent
import javax.swing.JTextField

final class StringField(value: String,
                        columns: Int,
                        fireChangeAction: String => Unit) extends JTextField(value):
  setText(value)
  setColumns(columns)

  addPropertyChangeListener(
    (_: PropertyChangeEvent) => fireChangeAction(getText().trim)
  )