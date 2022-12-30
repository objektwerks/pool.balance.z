package objektwerks.field

import java.beans.PropertyChangeEvent
import javax.swing.JFormattedTextField
import java.text.NumberFormat

final class DoubleRangeField(value: Double,
                             isInRange: Double => Boolean,
                             fireChangeAction: Double => Unit) extends JFormattedTextField( NumberFormat.getNumberInstance() ):
  setValue(value)
  setColumns(10)

  addPropertyChangeListener(
    (_: PropertyChangeEvent) =>
      val value = getValue.asInstanceOf[Number].doubleValue()
      if isInRange(value) then fireChangeAction(value)
  )