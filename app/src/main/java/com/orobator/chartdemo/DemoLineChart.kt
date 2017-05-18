package com.orobator.chartdemo

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.highlight.Highlight

/**
 * Created by AndrewOrobator on 5/12/17.
 */
class DemoLineChart @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
    defStyle: Int = 0) : LineChart(context,
    attrs, defStyle) {

  override fun highlightValue(highlight: Highlight?) {

  }
}