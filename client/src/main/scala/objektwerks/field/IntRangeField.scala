package objektwerks.field

import java.awt.Dimension
import java.beans.PropertyChangeEvent
import java.text.NumberFormat
import javax.swing.{InputVerifier, JComponent, JFormattedTextField}
import javax.swing.text.JTextComponent

final class IntRangeField(value: Int,
                     isInRange: Int => Boolean,
                     fireChangeAction: Int => Unit) extends JFormattedTextField( NumberFormat.getIntegerInstance() ):
  setPreferredSize( Dimension( 100, 40) )
  setValue(value)
  setColumns(10)
  setInputVerifier(
    new InputVerifier {
      private def isValid(input: JComponent): Boolean =
        input.asInstanceOf[JTextComponent].getText.toIntOption match
          case Some(value) => isInRange(value)
          case None => false

      override def verify(input: JComponent): Boolean = isValid(input)
      override def verifyTarget(input: JComponent): Boolean = isValid(input)
      override def shouldYieldFocus(source: JComponent, target: JComponent): Boolean = verify(source) || verifyTarget(target)
    }
  )

  addPropertyChangeListener(
    (event: PropertyChangeEvent) =>
      val value = event.getNewValue.asInstanceOf[Number].intValue
      if isInRange(value) then fireChangeAction(value)
  )