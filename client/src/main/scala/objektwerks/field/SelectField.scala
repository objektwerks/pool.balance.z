package objektwerks.field

import javax.swing.JComboBox

import scala.jdk.CollectionConverters.*

final class SelectField(values: List[String]) extends JComboBox[Object](values.asJava.toArray()):
  