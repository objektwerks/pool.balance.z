package objektwerks

import javax.swing.{JFrame, WindowConstants}

class Frame(title: String,
            width: Int,
            height: Int) extends JFrame:
  setTitle(title)
  setSize(width, height)
  setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  setLocationRelativeTo(null)