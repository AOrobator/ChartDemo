package com.orobator.chartdemo

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

/**
 * Created by AndrewOrobator on 5/12/17.
 */
class DemoMarkerView(context: Context) : MarkerView(context, R.layout.demo_marker_view) {
  val textView: TextView = findViewById(R.id.text) as TextView


  override fun refreshContent(e: Entry?, highlight: Highlight?) {
    super.refreshContent(e, highlight)

    textView.text = "Entry is ${e?.y}"
  }

  override fun getOffset(): MPPointF {
    return MPPointF.getInstance(-(width/2).toFloat(), -(height/2).toFloat())
  }
}