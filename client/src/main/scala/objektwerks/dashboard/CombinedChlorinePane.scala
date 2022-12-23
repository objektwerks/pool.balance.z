package objektwerks.dashboard

import objektwerks.Model.*

final class CombinedChlorinePane(title: String = "<html>Total<br>Chlorine") extends DashboardTitledPane(title):
  range.setText("0 - 10")
  good.setText("1 - 5")
  ideal.setText(("3"))

  currentCombinedChlorine.onChange { (_, _, newValue) =>
    current.setText(newValue.toString())
    if isCombinedChlorineInRange(newValue) then currentIsInRange
    else currentIsOutOfRange
  }

  averageCombinedChlorine.onChange { (_, _, newValue) =>
    average.setText(newValue.toString())
    if isCombinedChlorineInRange(newValue) then averageIsInRange
    else averageIsOutOfRange
  }