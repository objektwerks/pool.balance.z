package objektwerks

import java.awt.{GridLayout, Image}
import javax.swing.{JFrame, WindowConstants}

object Frame:
  val rows = 2
  val columns = 1
  val horizontalGap = 6
  val verticalGap = 6

final class Frame(logo: Image,
                  title: String,
                  width: Int,
                  height: Int) extends JFrame:
  import Frame.*

  setIconImage(logo)
  setTitle(title)
  setLayout( new GridLayout(rows, columns, horizontalGap, verticalGap) )
  setSize(width, height)
  setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  setLocationRelativeTo(null)
  setContentPane( View() )