package objektwerks.dashboard

import objektwerks.Model.*

final class CombinedChlorinePane(title: String = "<html>Combined<br>Chlorine") extends DashboardTitledPane(title):
  range.setText("0 - 0.5")
  good.setText("0.1 - 0.2")
  ideal.setText(("0"))

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