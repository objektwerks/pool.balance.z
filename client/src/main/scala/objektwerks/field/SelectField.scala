package objektwerks.field

import java.beans.PropertyChangeEvent
import java.util
import java.util.Vector
import javax.swing.JComboBox

import scala.jdk.CollectionConverters.*

final class SelectField(values: List[String],
                        fireChangeAction: String => Unit,
                        selectedIndex: Int = 0) extends JComboBox[String]( new util.Vector( values.asJava ) ):
  setSelectedIndex(selectedIndex)
  
  addPropertyChangeListener(
    (_: PropertyChangeEvent) => fireChangeAction( getSelectedItem.asInstanceOf[String] )
  )