package objektwerks.text

import javax.swing.JFormattedTextField
import java.text.NumberFormat

final class IntField(value: Int) extends JFormattedTextField( NumberFormat.getIntegerInstance() ):
  this.setValue(value)