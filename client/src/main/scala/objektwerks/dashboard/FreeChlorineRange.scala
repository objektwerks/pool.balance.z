package objektwerks.dashboard

import objektwerks.Model.*

final class FreeChlorineRange(title: String = "<html>Free<br>Chlorine") extends DashboardTitledPane(title):
  range.setText("0 - 10")
  good.setText("1 - 5")
  ideal.setText(("3"))

  currentFreeChlorine.onChange { (_, _, newValue) =>
    current.setText(newValue.toString())
    if isTotalChlorineInRange(newValue) then currentIsInRange
    else currentIsOutOfRange
  }

  averageFreeChlorine.onChange { (_, _, newValue) =>
    average.setText(newValue.toString())
    if isTotalChlorineInRange(newValue) then averageIsInRange
    else averageIsOutOfRange
  }