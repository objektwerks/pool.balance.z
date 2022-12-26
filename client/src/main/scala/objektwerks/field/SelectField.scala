package objektwerks.field

import java.util.Vector
import javax.swing.JComboBox

import scala.jdk.CollectionConverters.*

final class SelectField(values: List[String],
                        selectedIndex: Int = 0) extends JComboBox[String]( new Vector( values.asJava ) ):
  setSelectedIndex(selectedIndex)