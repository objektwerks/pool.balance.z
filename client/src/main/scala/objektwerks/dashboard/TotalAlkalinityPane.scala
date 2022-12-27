package objektwerks.dashboard

import objektwerks.Model.*

final class TotalAlkalinityPane(title: String = "<html>Total Alkalinity") extends DashboardTitledPane(title):
  range.setText("0 - 240")
  good.setText("80 - 120")
  ideal.setText("100")

  currentTotalAlkalinity.onChange { (_, _, newValue) =>
    current.setText(newValue.toString)
    if isTotalAlkalinityInRange(newValue) then currentIsInRange()
    else currentIsOutOfRange()
  }

  averageTotalAlkalinity.onChange { (_, _, newValue) =>
    average.setText(newValue.toString)
    if isTotalAlkalinityInRange(newValue) then averageIsInRange()
    else averageIsOutOfRange()
  }