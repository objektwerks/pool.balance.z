package objektwerks.field

import javax.swing.JFormattedTextField
import java.text.NumberFormat

final class DoubleField(value: Double) extends JFormattedTextField( NumberFormat.getNumberInstance() ):
  this.setValue(value)