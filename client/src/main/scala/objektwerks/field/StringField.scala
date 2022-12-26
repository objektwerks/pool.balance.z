package objektwerks.field

import javax.swing.JTextField

final class StringField(value: String,
                        columns: Int) extends JTextField(value):
  setColumns(columns)