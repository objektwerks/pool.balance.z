package objektwerks.dashboard

import objektwerks.Model.*

final class TotalAlkalinityPane(title: String = "<html>Total<br>Alkalinity") extends DashboardTitledPane(title):
  range.setText("0 - 10")
  good.setText("1 - 5")
  ideal.setText(("3"))

  currentTotalAlkalinity.onChange { (_, _, newValue) =>
    current.setText(newValue.toString())
    if isTotalAlkalinityInRange(newValue) then currentIsInRange
    else currentIsOutOfRange
  }

  averageTotalChlorine.onChange { (_, _, newValue) =>
    average.setText(newValue.toString())
    if isTotalAlkalinityInRange(newValue) then averageIsInRange
    else averageIsOutOfRange
  }