package com.orobator.chartdemo

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import android.support.v4.content.ContextCompat
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition.LEFT_BOTTOM
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition.LEFT_TOP
import com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM

class DataHistoryGraphActivity : AppCompatActivity() {

  companion object {
    fun launchDataHistoryGraph(context: Context) {
      val intent = Intent(context, DataHistoryGraphActivity::class.java)
      context.startActivity(intent)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_data_history_graph)

    val lineChart: LineChart = findViewById(R.id.line_chart) as LineChart

    val values: List<Entry> = (0..10).map { n ->
      if (n == 10) {
        Entry(n.toFloat(), fib(n).toFloat(),
            ContextCompat.getDrawable(this, R.drawable.ic_filter_tilt_shift_black_24dp))
      } else {
        Entry(n.toFloat(), fib(n).toFloat())
      }
    }

    val lineDataSet: LineDataSet = LineDataSet(values, "Used so far")
    lineDataSet.setDrawCircles(false)
    lineDataSet.setDrawValues(false)


    if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
      // fill drawable only supported on api level 18 and above
      val drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue)
      lineDataSet.fillDrawable = drawable
    } else {
      lineDataSet.fillColor = Color.BLACK
    }

    val dataSetLineWidth = 4f

    lineDataSet.setDrawFilled(true)
    lineDataSet.lineWidth = dataSetLineWidth
    lineDataSet.setDrawVerticalHighlightIndicator(false)
    lineDataSet.setDrawHorizontalHighlightIndicator(false)

    val projectedValues = listOf(Entry(10f, fib(10).toFloat()), Entry(14f, fib(11).toFloat()))
    val projectedLineDataSet = LineDataSet(projectedValues, "Projected")

    if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
      // fill drawable only supported on api level 18 and above
      val drawable = ContextCompat.getDrawable(this, R.drawable.fade_black)
      projectedLineDataSet.fillDrawable = drawable
    } else {
      projectedLineDataSet.fillColor = Color.BLACK
    }

    projectedLineDataSet.setDrawFilled(true)
    projectedLineDataSet.setDrawValues(false)
    projectedLineDataSet.setDrawCircles(false)
    projectedLineDataSet.lineWidth = dataSetLineWidth
    projectedLineDataSet.setDrawVerticalHighlightIndicator(false)
    projectedLineDataSet.setDrawHorizontalHighlightIndicator(false)

    lineChart.data = LineData(listOf(projectedLineDataSet, lineDataSet))

    val monthlyCapLimitLine = LimitLine(100f, "Monthly cap")
    monthlyCapLimitLine.lineWidth = 4f
    monthlyCapLimitLine.enableDashedLine(10f, 10f, 0f)
    monthlyCapLimitLine.labelPosition = LEFT_TOP
    lineChart.axisLeft.addLimitLine(monthlyCapLimitLine)
    lineChart.axisLeft.axisMinimum = 0f
    lineChart.axisLeft.setDrawAxisLine(false)

//    lineChart.axisLeft.setDrawGridLines(false)

    val projectedLimitLine = LimitLine(12f, "1 day early")
    projectedLimitLine.lineWidth = 4f
    projectedLimitLine.enableDashedLine(10f, 10f, 0f)
    projectedLimitLine.labelPosition = LEFT_BOTTOM

    lineChart.xAxis.addLimitLine(projectedLimitLine)
    lineChart.xAxis.setDrawGridLines(false)

    lineChart.isDragEnabled = true
    lineChart.setScaleEnabled(false)
    lineChart.axisRight.isEnabled = false

    lineChart.xAxis.setValueFormatter {
      value, _ ->
      "4/${(value + 15).toInt()}"

    }

    lineChart.xAxis.position = BOTTOM
    lineChart.xAxis.setDrawAxisLine(false)
    lineChart.description.text = ""
    lineChart.legend.isEnabled = false

    lineChart.marker = DemoMarkerView(this)
  }

  fun fib(n: Int): Int =
      when (n) {
        0 -> 1
        1 -> 1
        else -> fib(n - 1) + fib(n - 2)
      }
}
