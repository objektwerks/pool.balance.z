package objektwerks.field

import java.awt.Dimension
import java.beans.PropertyChangeEvent
import java.util
import java.util.Vector
import javax.swing.JComboBox

import scala.jdk.CollectionConverters.*

final class SelectField(values: List[String],
                        fireChangeAction: String => Unit,
                        selectedIndex: Option[(String, Int)]) extends JComboBox[String]( util.Vector( values.asJava ) ):
  setPreferredSize( Dimension( 100, 40) )
  selectedIndex match
    case Some( (_, index) ) => setSelectedIndex(index)
    case None => setSelectedIndex(0)
  
  addPropertyChangeListener(
    (_: PropertyChangeEvent) => fireChangeAction( getSelectedItem.asInstanceOf[String] )
  )