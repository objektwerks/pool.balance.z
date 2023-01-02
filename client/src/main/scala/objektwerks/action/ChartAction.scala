package objektwerks.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

import org.jfree.chart.ChartPanel

import objektwerks.dialog.ChartDialog

final class ChartAction(name: String,
                        title: String,
                        builder: => ChartPanel) extends AbstractAction(name):
  override def actionPerformed(event: ActionEvent): Unit = ChartDialog(title, builder).open()