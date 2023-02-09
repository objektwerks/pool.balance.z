package objektwerks

import java.awt.Image
import javax.swing.{JFrame, WindowConstants}

final class Frame(logo: Image,
                  title: String,
                  width: Int,
                  height: Int) extends JFrame:
  setIconImage(logo)
  setTitle(title)
  setSize(width, height)
  setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  setLocationRelativeTo(null)
  setJMenuBar( Menu(this) )
  setContentPane( View() )