package objektwerks.field

import java.beans.PropertyChangeEvent
import java.text.NumberFormat
import javax.swing.{InputVerifier, JComponent, JFormattedTextField}
import javax.swing.text.JTextComponent

final class DoubleRangeField(value: Double,
                             isInRange: Double => Boolean,
                             fireChangeAction: Double => Unit) extends JFormattedTextField( NumberFormat.getNumberInstance() ):
  setValue(value)
  setColumns(10)
  setInputVerifier(
    new InputVerifier {
      private def isValid(input: JComponent): Boolean =
        input.asInstanceOf[JTextComponent].getText.toDoubleOption match
          case Some(value) => isInRange(value)
          case None => false

      override def verify(input: JComponent): Boolean = isValid(input)
      override def verifyTarget(input: JComponent): Boolean = isValid(input)
      override def shouldYieldFocus(source: JComponent, target: JComponent): Boolean = verify(source) || verifyTarget(target)
    }
  )

  addPropertyChangeListener(
    (_: PropertyChangeEvent) =>
      val value = getValue.asInstanceOf[Number].doubleValue()
      if isInRange(value) then fireChangeAction(value)
  )