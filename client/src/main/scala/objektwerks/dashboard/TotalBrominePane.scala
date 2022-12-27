package objektwerks.dashboard

import objektwerks.Model.*

final class TotalBrominePane(title: String = "<html>Total Bromine") extends DashboardTitledPane(title):
  range.setText("0 - 20")
  good.setText("2 - 10")
  ideal.setText("5")

  currentTotalBromine.onChange { (_, _, newValue) =>
    current.setText(newValue.toString)
    if isTotalBromineInRange(newValue) then currentIsInRange()
    else currentIsOutOfRange()
  }

  averageTotalBromine.onChange { (_, _, newValue) =>
    average.setText(newValue.toString)
    if isTotalBromineInRange(newValue) then averageIsInRange()
    else averageIsOutOfRange()
  }