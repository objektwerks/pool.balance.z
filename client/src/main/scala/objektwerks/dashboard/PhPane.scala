package objektwerks.dashboard

import objektwerks.Model.*

final class PhPane(title: String = "<html>Ph") extends DashboardTitledPane(title):
  range.setText("0 - 10")
  good.setText("1 - 5")
  ideal.setText(("3"))

  currentPh.onChange { (_, _, newValue) =>
    current.setText(newValue.toString())
    if isPhInRange(newValue) then currentIsInRange
    else currentIsOutOfRange
  }

  averageTotalChlorine.onChange { (_, _, newValue) =>
    average.setText(newValue.toString())
    if isPhInRange(newValue) then averageIsInRange
    else averageIsOutOfRange
  }