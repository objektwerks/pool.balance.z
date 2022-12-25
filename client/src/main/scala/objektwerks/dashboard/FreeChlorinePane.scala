package objektwerks.dashboard

import objektwerks.Model.*

final class FreeChlorinePane(title: String = "<html>Free Chlorine") extends DashboardTitledPane(title):
  range.setText("0 - 10")
  good.setText("1 - 5")
  ideal.setText(("3"))

  currentFreeChlorine.onChange { (_, _, newValue) =>
    current.setText(newValue.toString())
    if isFreeChlorineInRange(newValue) then currentIsInRange
    else currentIsOutOfRange
  }

  averageFreeChlorine.onChange { (_, _, newValue) =>
    average.setText(newValue.toString())
    if isFreeChlorineInRange(newValue) then averageIsInRange
    else averageIsOutOfRange
  }