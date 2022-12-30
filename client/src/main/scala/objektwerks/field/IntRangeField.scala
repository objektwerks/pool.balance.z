package objektwerks.field

import java.beans.PropertyChangeEvent
import javax.swing.JFormattedTextField
import java.text.NumberFormat

final class IntRangeField(value: Int,
                     isInRange: Int => Boolean,
                     fireChangeAction: Int => Unit) extends JFormattedTextField( NumberFormat.getIntegerInstance() ):
  setValue(value)
  setColumns(10)

  addPropertyChangeListener(
    (_: PropertyChangeEvent) =>
      val value = getValue.asInstanceOf[Number].intValue()
      if isInRange(value) then fireChangeAction(value)
  )