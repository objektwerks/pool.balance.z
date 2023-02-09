package objektwerks.field

import java.awt.Dimension
import java.beans.PropertyChangeEvent
import javax.swing.JFormattedTextField
import java.text.NumberFormat

final class IntField(value: Int,
                     fireChangeAction: Int => Unit) extends JFormattedTextField( NumberFormat.getIntegerInstance() ):
  setPreferredSize( Dimension( 100, 40) )
  setValue(value)
  setColumns(10)

  addPropertyChangeListener(
    (_: PropertyChangeEvent) => fireChangeAction(getValue.asInstanceOf[Number].intValue())
  )