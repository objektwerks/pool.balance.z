package objektwerks.dashboard

import objektwerks.Context.*
import objektwerks.Model.*

final class SaltPane(title: String = salt.asHtml) extends DashboardTitledPane(title):
  range.setText("0 - 3600")
  good.setText("2700 - 3400")
  ideal.setText("3200")

  currentSalt.onChange { (_, _, newValue) =>
    current.setText(newValue.toString)
    if isSaltInRange(newValue) then currentIsInRange()
    else currentIsOutOfRange()
  }

  averageSalt.onChange { (_, _, newValue) =>
    average.setText(newValue.toString)
    if isSaltInRange(newValue) then averageIsInRange()
    else averageIsOutOfRange()
  }