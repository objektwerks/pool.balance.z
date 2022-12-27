package objektwerks.field

import java.beans.PropertyChangeEvent
import javax.swing.JFormattedTextField
import java.text.NumberFormat

final class IntField(value: Int,
                     fireChangeAction: Int => Unit) extends JFormattedTextField( NumberFormat.getIntegerInstance() ):
  setValue(value)
  setColumns(10)

  addPropertyChangeListener(
    (_: PropertyChangeEvent) => fireChangeAction(getValue.asInstanceOf[Number].intValue())
  )