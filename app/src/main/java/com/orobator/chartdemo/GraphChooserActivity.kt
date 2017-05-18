package com.orobator.chartdemo

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.orobator.chartdemo.R.id

class GraphChooserActivity : AppCompatActivity() {
  private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
    when (item.itemId) {
      R.id.navigation_home -> {
        selectFragment(DataHistoryGraphFragment())
        return@OnNavigationItemSelectedListener true
      }
      R.id.navigation_dashboard -> {
        selectFragment(RamUsageFragment())
        return@OnNavigationItemSelectedListener true
      }
      R.id.navigation_notifications -> {
        selectFragment(CircleGraphFragment())
        return@OnNavigationItemSelectedListener true
      }
    }
    false
  }

  private fun selectFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .replace(id.content, fragment)
        .commit()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_graph_chooser)

    val navigation = findViewById(R.id.navigation) as BottomNavigationView
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    selectFragment(DataHistoryGraphFragment())
  }
}
