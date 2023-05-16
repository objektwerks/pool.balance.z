package objektwerks.chart

import java.awt.Color
import java.text.{DecimalFormat, SimpleDateFormat}
import java.util.Date
import javax.swing.BorderFactory

import org.jfree.chart.{ChartPanel, JFreeChart}
import org.jfree.chart.labels.{StandardXYItemLabelGenerator, XYItemLabelGenerator}
import org.jfree.chart.axis.{DateAxis, NumberAxis}
import org.jfree.chart.labels.StandardXYToolTipGenerator
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.{XYItemRenderer, XYLineAndShapeRenderer}
import org.jfree.data.time.{Day, TimeSeries, TimeSeriesCollection}
import org.jfree.data.xy.XYDataset

import scala.math.abs

import objektwerks.{Context, Entity, Model}

object Chart:
  def buildTotalChlorineChart: ChartPanel =
    val data = Model.observableMeasurements.map(m => ( Entity.date(m.measured), m.totalChlorine.toDouble ) ).toList
    val title = Context.totalChlorine
    build(data, title)

  def buildFreeChlorineChart: ChartPanel = 
    val data = Model.observableMeasurements.map(m => ( Entity.date(m.measured), m.freeChlorine.toDouble ) ).toList
    val title = Context.freeChlorine
    build(data, title)

  def buildCombinedChlorineChart: ChartPanel = 
    val data = Model.observableMeasurements.map(m => ( Entity.date(m.measured), m.combinedChlorine ) ).toList
    val title = Context.combinedChlorine
    build(data, title)

  def buildPhChart: ChartPanel =
    val data = Model.observableMeasurements.map(m => ( Entity.date(m.measured), m.ph ) ).toList
    val title = Context.ph
    build(data, title)

  def buildCalciumHardnessChart: ChartPanel =
    val data = Model.observableMeasurements.map(m => ( Entity.date(m.measured), m.calciumHardness.toDouble ) ).toList
    val title = Context.calciumHardness
    build(data, title)

  def buildTotalAlkalinityChart: ChartPanel =
    val data = Model.observableMeasurements.map(m => ( Entity.date(m.measured), m.totalAlkalinity.toDouble ) ).toList
    val title = Context.totalAlkalinity
    build(data, title)

  def buildCyanuricAcidChart: ChartPanel =
    val data = Model.observableMeasurements.map(m => ( Entity.date(m.measured), m.cyanuricAcid.toDouble ) ).toList
    val title = Context.cyanuricAcid
    build(data, title)

  def buildTotalBromineChart: ChartPanel =
    val data = Model.observableMeasurements.map(m => ( Entity.date(m.measured), m.totalBromine.toDouble ) ).toList
    val title = Context.totalBromine
    build(data, title)

  def buildSaltCharrt: ChartPanel =
    val data = Model.observableMeasurements.map(m => ( Entity.date(m.measured), m.salt.toDouble ) ).toList
    val title = Context.salt
    build(data, title)

  def buildTemperatureChart: ChartPanel =
    val data = Model.observableMeasurements.map(m => ( Entity.date(m.measured), m.temperature.toDouble ) ).toList
    val title = Context.temperature
    build(data, title)

  private def build(measurements: List[(Date, Double)],
                    title: String): ChartPanel =
    val xyPlot = XYPlot()
    xyPlot.setDataset( buildDataset(measurements, title) )
    xyPlot.setRenderer( buildRenderer(title) )

    val xAxis = DateAxis(Context.date)
    xAxis.setDateFormatOverride( SimpleDateFormat("d,H") )
    xyPlot.setDomainAxis(xAxis)

    val yAxis = NumberAxis(title)
    xyPlot.setRangeAxis(yAxis)

    val chart = JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, xyPlot, true)
    buildChartPanel(chart)

  private def buildChartPanel(chart: JFreeChart): ChartPanel =
    chart.getPlot.setBackgroundPaint(Color.LIGHT_GRAY)
    val chartPanel = ChartPanel(chart)
    chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15))
    chartPanel.setInitialDelay(100)
    chartPanel.setReshowDelay(100)
    chartPanel.setDismissDelay(10000)
    chartPanel

  private def buildDataset(measurements: List[(Date, Double)],
                           title: String): XYDataset =
    val timeSeries = TimeSeries(title)
    measurements.foreach { (measured, measurement) =>
      timeSeries.add( Day(measured), measurement )
    }
    TimeSeriesCollection(timeSeries)

  private def buildRenderer(title: String): XYItemRenderer =
    val renderer = XYLineAndShapeRenderer()
    val tooltipGenerator = new StandardXYToolTipGenerator() {
      override def generateToolTip(dataset: XYDataset, series: Int, item: Int): String =
        val xValue = dataset.getXValue(series, item)
        val yValue = dataset.getYValue(series, item)
        val dayHourMinute = SimpleDateFormat("d,H:m").format( Date( xValue.toLong ) )
        val beatsPerMinute = DecimalFormat("0").format( yValue )
        val delta = calculateDeltaAsPercentage(dataset, series, item)
        s"${title}: ($dayHourMinute, $beatsPerMinute, $delta%)"
      override def clone() = this
    }
    renderer.setDefaultToolTipGenerator(tooltipGenerator)
    renderer.setDefaultShapesVisible(true)
    renderer.setDefaultItemLabelGenerator( buildItemLabelGenerator("0") )
    renderer.setDefaultItemLabelsVisible(true)
    renderer

  private def buildItemLabelGenerator(decimalFormat: String): XYItemLabelGenerator =
    new StandardXYItemLabelGenerator() {
      override def generateLabel(dataset: XYDataset, series: Int, item: Int): String =
        val yValue = dataset.getYValue(series, item)
        DecimalFormat(decimalFormat).format( yValue )
      override def clone() = this
    }

  private def calculateDeltaAsPercentage(dataset: XYDataset,
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