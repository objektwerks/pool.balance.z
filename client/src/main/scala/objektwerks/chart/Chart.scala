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

import objektwerks.{Context, Entity, Measurement, Model}

object Chart:
  val totalChlorineDate = Model.observableMeasurements.map(m => ( Entity.date(m.measured), m.totalChlorine.toDouble ) )
  val totalChlorineTitle = Context.totalChlorine

  val freeChlorineDate = Model.observableMeasurements.map(m => ( Entity.date(m.measured), m.freeChlorine.toDouble ) )
  val freeChlorineTitle = Context.freeChlorine

  val combinedChlorineDate = Model.observableMeasurements.map(m => ( Entity.date(m.measured), m.combinedChlorine ) )
  val combinedChlorineTitle = Context.combinedChlorine

  val phDate = Model.observableMeasurements.map(m => ( Entity.date(m.measured), m.ph ) )
  val phTitle = Context.ph

  val calciumHardnessDate = Model.observableMeasurements.map(m => ( Entity.date(m.measured), m.calciumHardness.toDouble ) )
  val calciumHardnessTitle = Context.calciumHardness

  val totalAlkalinityDate = Model.observableMeasurements.map(m => ( Entity.date(m.measured), m.totalAlkalinity.toDouble ) )
  val totalAlkalinityTitle = Context.totalAlkalinity

  val cyanuricAcidDate = Model.observableMeasurements.map(m => ( Entity.date(m.measured), m.cyanuricAcid.toDouble ) )
  val cyanuricAcidTitle = Context.cyanuricAcid

  val totalBromineDate = Model.observableMeasurements.map(m => ( Entity.date(m.measured), m.totalBromine.toDouble ) )
  val totalBromineTitle = Context.totalBromine

  val saltDate = Model.observableMeasurements.map(m => ( Entity.date(m.measured), m.salt.toDouble ) )
  val saltTitle = Context.salt

  def build(measurements: List[(Date, Double)],
            title: String): ChartPanel =
    val xyPlot = new XYPlot()
    xyPlot.setDataset( buildDataset(measurements, title) )
    xyPlot.setRenderer( buildRenderer(title) )

    val xAxis = new DateAxis(Context.date)
    xAxis.setDateFormatOverride( new SimpleDateFormat("d,H") )
    xyPlot.setDomainAxis(xAxis)

    val yAxis = new NumberAxis(title)
    xyPlot.setRangeAxis(yAxis)

    val chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, xyPlot, true)
    buildChartPanel(chart)

  private def buildChartPanel(chart: JFreeChart): ChartPanel =
    chart.getPlot.setBackgroundPaint(Color.LIGHT_GRAY)
    val chartPanel = new ChartPanel(chart)
    chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15))
    chartPanel.setInitialDelay(100)
    chartPanel.setReshowDelay(100)
    chartPanel.setDismissDelay(10000)
    chartPanel

  private def buildDataset(measurements: List[(Date, Double)],
                           title: String): XYDataset =
    val timeSeries = new TimeSeries(title)
    measurements.foreach { (measured, measurement) =>
      timeSeries.add( new Day(measured), measurement )
    }
    new TimeSeriesCollection(timeSeries)

  private def buildRenderer(title: String): XYItemRenderer =
    val renderer = new XYLineAndShapeRenderer()
    val tooltipGenerator = new StandardXYToolTipGenerator() {
      override def generateToolTip(dataset: XYDataset, series: Int, item: Int): String =
        val xValue = dataset.getXValue(series, item)
        val yValue = dataset.getYValue(series, item)
        val dayHourMinute = new SimpleDateFormat("d,H:m").format( new Date( xValue.toLong ) )
        val beatsPerMinute = new DecimalFormat("0").format( yValue )
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
        new DecimalFormat(decimalFormat).format( yValue )
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