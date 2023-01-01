package objektwerks.chart

import java.text.DecimalFormat

import org.jfree.chart.labels.{StandardXYItemLabelGenerator, XYItemLabelGenerator}
import org.jfree.data.xy.XYDataset

import scala.math.abs

abstract class Chart:
  def buildItemLabelGenerator(decimalFormat: String): XYItemLabelGenerator =
    new StandardXYItemLabelGenerator() {
      override def generateLabel(dataset: XYDataset, series: Int, item: Int): String =
        val yValue = dataset.getYValue(series, item)
        new DecimalFormat(decimalFormat).format( yValue )
      override def clone() = this
    }

  def calculateDeltaAsPercentage(dataset: XYDataset,
                                 series: Int,
                                 item: Int): Long =
    val datasetLastIndex = dataset.getItemCount(series) - 1
    val previousItemIndex = item - 1
    val datasetIndexRange = Range.inclusive(0, datasetLastIndex)
    if ( datasetIndexRange.contains( previousItemIndex ) ) then
      val yCurrentValue = dataset.getYValue(series, item)
      val yPreviousValue = dataset.getYValue(series, previousItemIndex)
      val yDividendValue = yCurrentValue - yPreviousValue
      val yDivisorValue = ( yCurrentValue + yPreviousValue ) / 2
      val yValueDelta = abs( yDividendValue / yDivisorValue ) * 100
      yValueDelta.round
    else 0L