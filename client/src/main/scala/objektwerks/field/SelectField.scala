package objektwerks.field

import javax.swing.JComboBox

import scala.jdk.CollectionConverters.*

final class SelectField(values: List[String],
                        selectedIndex: Int = 0) extends JComboBox[Object](values.asJava.toArray()):
  setSelectedIndex(selectedIndex)