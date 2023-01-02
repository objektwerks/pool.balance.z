package objektwerks.dashboard

import objektwerks.Context.*
import objektwerks.Model.*

final class CyanuricAcidPane(title: String = cyanuricAcid.asHtml) extends DashboardTitledPane(title):
  range.setText("0 - 300")
  good.setText("30 - 100")
  ideal.setText("50")

  currentCyanuricAcid.onChange { (_, _, newValue) =>
    current.setText(newValue.toString)
    if isCyanuricAcidInRange(newValue) then currentIsInRange()
    else currentIsOutOfRange()
  }

  averageCyanuricAcid.onChange { (_, _, newValue) =>
    average.setText(newValue.toString)
    if isCyanuricAcidInRange(newValue) then averageIsInRange()
    else averageIsOutOfRange()
  }