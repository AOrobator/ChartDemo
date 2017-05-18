package com.orobator.chartdemo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar

/**
 * Created by AndrewOrobator on 5/16/17.
 */
class CircleGraphFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_circle_graph, container, false)

    val circleGraphView = view.findViewById(R.id.circle_graph) as CircleGraphView

    val seekBar = view.findViewById(R.id.seek_bar) as SeekBar
    seekBar.max = 100
    seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
      override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        circleGraphView.setFraction(progress / 100f)
      }

      override fun onStartTrackingTouch(seekBar: SeekBar?) {}

      override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    })

    return view
  }
}