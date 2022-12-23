package objektwerks.dashboard

import objektwerks.Model.*

final class CalciumHardnessPane(title: String = "<html>Calcium<br>Hardness") extends DashboardTitledPane(title):
  range.setText("0 - 1000")
  good.setText("200 - 500")
  ideal.setText(("375"))

  currentCalciumHardness.onChange { (_, _, newValue) =>
    current.setText(newValue.toString())
    if isCalciumHardnessInRange(newValue) then currentIsInRange
    else currentIsOutOfRange
  }

  averageCalciumHardness.onChange { (_, _, newValue) =>
    average.setText(newValue.toString())
    if isCalciumHardnessInRange(newValue) then averageIsInRange
    else averageIsOutOfRange
  }