package objektwerks.dashboard

import objektwerks.Context.*
import objektwerks.Model.*

final class TemperaturePane(title: String = temperature) extends DashboardTitledPane(title):
  range.setText("32 - 110")
  good.setText("75 - 85")
  ideal.setText("83")

  currentTemperature.onChange { (_, _, newValue) =>
    current.setText(newValue.toString)
    if isTemperatureInRange(newValue) then currentIsInRange()
    else currentIsOutOfRange()
  }

  averageTemperature.onChange { (_, _, newValue) =>
    average.setText(newValue.toString)
    if isTemperatureInRange(newValue) then averageIsInRange()
    else averageIsOutOfRange()
  }