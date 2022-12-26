package objektwerks.field

import javax.swing.JFormattedTextField
import java.text.NumberFormat

final class IntField(value: Int,
                     columns: Int = 10) extends JFormattedTextField( NumberFormat.getIntegerInstance() ):
  setValue(value)
  setColumns(columns)