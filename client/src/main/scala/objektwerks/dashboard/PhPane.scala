package objektwerks.dashboard

import objektwerks.Model.*

final class PhPane(title: String = "<html>Ph") extends DashboardTitledPane(title):
  range.setText("6.2 - 8.4")
  good.setText("7.2 - 7.6")
  ideal.setText("7.4")

  currentPh.onChange { (_, _, newValue) =>
    current.setText(newValue.toString)
    if isPhInRange(newValue) then currentIsInRange()
    else currentIsOutOfRange()
  }

  averagePh.onChange { (_, _, newValue) =>
    average.setText(newValue.toString)
    if isPhInRange(newValue) then averageIsInRange()
    else averageIsOutOfRange()
  }