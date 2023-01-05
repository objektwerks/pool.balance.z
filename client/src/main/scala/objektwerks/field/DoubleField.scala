package objektwerks.field

import java.awt.Dimension
import java.beans.PropertyChangeEvent
import javax.swing.JFormattedTextField
import java.text.NumberFormat

final class DoubleField(value: Double,
                        fireChangeAction: Double => Unit) extends JFormattedTextField( NumberFormat.getNumberInstance() ):
  setPreferredSize( Dimension( 100, 40) )
  setValue(value)
  setColumns(10)

  addPropertyChangeListener(
    (_: PropertyChangeEvent) => fireChangeAction( getValue.asInstanceOf[Number].doubleValue() )
  )