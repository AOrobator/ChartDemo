package com.orobator.chartdemo

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jaredrummler.android.processes.AndroidProcesses

/**
 * Created by AndrewOrobator on 5/15/17.
 */
class RamUsageFragment : Fragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {

    val view = inflater.inflate(R.layout.fragment_ram_usage, container, false)
    val recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView

    val activityManager: ActivityManager = activity.getSystemService(
        Context.ACTIVITY_SERVICE) as ActivityManager

    val runningProcesses = activityManager.runningAppProcesses
    recyclerView.adapter = RamUsageRecyclerAdapter(AndroidProcesses.getRunningAppProcesses())

    val totalRamTextView = view.findViewById(R.id.total_ram_text_view) as TextView
    val memoryInfo = ActivityManager.MemoryInfo()
    activityManager.getMemoryInfo(memoryInfo)

    totalRamTextView.text = "Total memory: ${memoryInfo.totalMem}\nAvailable Memory: ${memoryInfo.availMem}"

    return view
  }
}