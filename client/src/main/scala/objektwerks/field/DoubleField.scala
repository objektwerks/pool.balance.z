package objektwerks.field

import javax.swing.JFormattedTextField
import java.text.NumberFormat

final class DoubleField(value: Double,
                        columns: Int = 10) extends JFormattedTextField( NumberFormat.getNumberInstance() ):
  setValue(value)
  setColumns(columns)