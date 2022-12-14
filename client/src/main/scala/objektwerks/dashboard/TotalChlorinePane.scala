package objektwerks.dashboard

import objektwerks.Context.*
import objektwerks.Model.*

final class TotalChlorinePane(title: String = totalChlorine) extends DashboardTitledPane(title):
  range.setText("0 - 10")
  good.setText("1 - 5")
  ideal.setText("3")

  currentTotalChlorine.onChange { (_, _, newValue) =>
    current.setText(newValue.toString)
    if isTotalChlorineInRange(newValue) then currentIsInRange()
    else currentIsOutOfRange()
  }

  averageTotalChlorine.onChange { (_, _, newValue) =>
    average.setText(newValue.toString)
    if isTotalChlorineInRange(newValue) then averageIsInRange()
    else averageIsOutOfRange()
  }