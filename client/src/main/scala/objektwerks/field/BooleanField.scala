package objektwerks.field

import java.beans.PropertyChangeEvent
import javax.swing.JCheckBox

final class BooleanField(state: Boolean,
                         fireChangeAction: Boolean => Unit) extends JCheckBox:
  setSelected(state)

  addPropertyChangeListener(
    (_: PropertyChangeEvent) => fireChangeAction(isSelected)
  )