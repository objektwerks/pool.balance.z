package objektwerks.chart

import java.text.{DecimalFormat, SimpleDateFormat}
import java.util.Date

import org.jfree.chart.JFreeChart
import org.jfree.chart.axis.{DateAxis, NumberAxis}
import org.jfree.chart.labels.StandardXYToolTipGenerator
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.{XYItemRenderer, XYLineAndShapeRenderer}
import org.jfree.data.time.{Day, TimeSeries, TimeSeriesCollection}
import org.jfree.data.xy.XYDataset

import objektwerks.{Context, Entity, Measurement}

object TotalChlorineChart extends Chart:
  def build(measurements: List[(Date, Double)], title: String): JFreeChart =
    val xyPlot = new XYPlot()
    xyPlot.setDataset( buildDataset(measurements, title) )
    xyPlot.setRenderer( buildRenderer(title) )

    val xAxis = new DateAxis(Context.date)
    xAxis.setDateFormatOverride( new SimpleDateFormat("d,H") )
    xyPlot.setDomainAxis(xAxis)

    val yAxis = new NumberAxis(title)
    xyPlot.setRangeAxis(yAxis)

    new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, xyPlot, true)

  def buildDataset(measurements: List[(Date, Double)], title: String): XYDataset =
    val timeSeries = new TimeSeries(title)
    measurements.foreach { (measured, measurement) =>
      timeSeries.add( new Day(measured), measurement )
    }
    new TimeSeriesCollection(timeSeries)

  def buildRenderer(title: String): XYItemRenderer =
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