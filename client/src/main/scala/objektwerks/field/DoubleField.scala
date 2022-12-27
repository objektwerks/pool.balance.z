package objektwerks.field

import java.beans.PropertyChangeEvent
import javax.swing.JFormattedTextField
import java.text.NumberFormat

final class DoubleField(value: Double,
                        columns: Int = 10,
                        fireChangeAction: Double => Unit) extends JFormattedTextField( NumberFormat.getNumberInstance() ):
  setValue(value)
  setColumns(columns)

  addPropertyChangeListener(
    (_: PropertyChangeEvent) => fireChangeAction( getValue.asInstanceOf[Number].doubleValue() )
  )