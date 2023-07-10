package com.example.mobitail.retailer.mainActivities

import android.os.Bundle
import com.example.mobitail.R
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity

import com.google.android.material.bottomnavigation.BottomNavigationView

import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter


class RetailDashboardActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var chart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        chart = findViewById(R.id.barChart)

        setupChart()
        val entries = generateChartData()
        setData(entries)


        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.action_dashboard

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_accounts -> {
                    val intent = Intent(this, RetailAccountsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    true
                }

                R.id.action_dashboard -> {
                    val intent = Intent(this, RetailDashboardActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    true
                }

                R.id.action_products -> {
                    val intent = Intent(this, ProductsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    true
                }

                R.id.action_settings -> {
                    val intent = Intent(this, RetailSettingsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    true
                }

                else -> false
            }
        }
    }

    private fun setupChart() {
        chart.setBackgroundColor(Color.BLACK)

        chart.apply {
            setDrawBarShadow(false)
            setDrawValueAboveBar(true)
            description.isEnabled = false
            setPinchZoom(false)
            setDrawGridBackground(false)
            setDrawBorders(false)
            animateY(2000)
            legend.isEnabled = false
        }

        val xAxis: XAxis = chart.xAxis
        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            granularity = 1f
            isGranularityEnabled = true
            valueFormatter = DayAxisValueFormatter()
            textColor = Color.WHITE
            axisLineWidth = 1f
            axisLineColor = Color.WHITE
            setLabelCount(7, false)
        }
        xAxis.axisMinimum = -0.5f

        val yAxisLeft: YAxis = chart.axisLeft
        val yAxisRight: YAxis = chart.axisRight
        yAxisLeft.apply {
            setDrawGridLines(true)
            axisMinimum = 0f
            textColor = Color.WHITE
            valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return value.toInt().toString()
                }
            }
            axisLineWidth = 1f
            axisLineColor = Color.WHITE
        }
        yAxisRight.isEnabled = false
    }

    private fun setData(entries: List<BarEntry>) {
        val dataSet = BarDataSet(entries, "Weekly Sales")

        dataSet.apply {
            color = Color.GREEN
            valueTextColor = Color.WHITE
        }

        val data = BarData(dataSet)

        chart.data = data

        chart.apply {
            description.text = "Store Sales Performance This Week"
            description.textColor = Color.WHITE
            description.textSize = 12f
            description.isEnabled = true
            description.setPosition(550f, 25f)
        }

        chart.invalidate()
    }



    private fun generateChartData(): List<BarEntry> {
        val entries = mutableListOf<BarEntry>()

        entries.add(BarEntry(0f, 10f))
        entries.add(BarEntry(1f, 20f))
        entries.add(BarEntry(2f, 15f))
        entries.add(BarEntry(3f, 25f))
        entries.add(BarEntry(4f, 30f))
        entries.add(BarEntry(5f, 18f))
        entries.add(BarEntry(6f, 22f))

        return entries
    }

    private inner class DayAxisValueFormatter : ValueFormatter() {
        private val days = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()) ?: value.toString()
        }
    }
}

