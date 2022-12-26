package objektwerks.text

import javax.swing.JFormattedTextField
import java.text.NumberFormat

final class DoubleTextField(value: Double) extends JFormattedTextField( NumberFormat.getNumberInstance() ):
  this.setValue(value)