package objektwerks.dashboard

import objektwerks.Model.*

final class TotalChlorinePane(title: String = "<html>Total<br>Chlorine") extends DashboardTitledPane(title):
  range.setText("0 - 10")
  good.setText("1 - 5")
  ideal.setText(("3"))

  currentTotalChlorine.onChange { (_, _, newValue) =>
    current.setText(newValue.toString())
    if totalChlorineInRange(newValue) then inRangeCurrent else outOfRangeCurrent
  }

  averageTotalChlorine.onChange { (_, _, newValue) =>
    average.setText(newValue.toString())
    if totalChlorineInRange(newValue) then inRangeAverage else outOfRangeAverage
  }