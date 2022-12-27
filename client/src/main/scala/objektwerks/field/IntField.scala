package objektwerks.field

import java.beans.PropertyChangeEvent
import javax.swing.JFormattedTextField
import java.text.NumberFormat

final class IntField(value: Int,
                     columns: Int = 10,
                     fireChangeAction: Int => Unit) extends JFormattedTextField( NumberFormat.getIntegerInstance() ):
  setValue(value)
  setColumns(columns)

  addPropertyChangeListener(
    (_: PropertyChangeEvent) => fireChangeAction(getValue.asInstanceOf[Number].intValue())
  )