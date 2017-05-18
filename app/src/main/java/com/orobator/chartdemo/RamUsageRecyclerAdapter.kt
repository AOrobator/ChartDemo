package com.orobator.chartdemo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jaredrummler.android.processes.models.AndroidAppProcess
import com.orobator.chartdemo.RamUsageRecyclerAdapter.ViewHolder

/**
 * Created by AndrewOrobator on 5/15/17.
 */
class RamUsageRecyclerAdapter(
    val processInfos: MutableList<AndroidAppProcess>) : RecyclerView.Adapter<ViewHolder>() {

  override fun getItemCount(): Int = processInfos.size


  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
    val layoutInflater = LayoutInflater.from((parent as View).context)
    val view = layoutInflater.inflate(R.layout.ram_usage_list_item, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int): Unit = holder.bind(position)


  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val pidTextView: TextView = itemView.findViewById(R.id.pid_text_view) as TextView
    val processNameTextView: TextView = itemView.findViewById(
        R.id.process_name_text_view) as TextView

    fun bind(position: Int) {
      val processInfo = processInfos[position]
      pidTextView.text = "PID: ${processInfo.pid}"
      processNameTextView.text = "Process name: ${processInfo.packageName}"
    }
  }
}