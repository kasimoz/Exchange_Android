package com.kasim.exchangeandroid.chartFormatter

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import com.kasim.exchangeandroid.helpers.toStringFormat


class DateChartFormatter(dateArray: ArrayList<String>?) : ValueFormatter() {

    var dateArray: ArrayList<String>? = null

    init {
        this.dateArray = dateArray
    }

    override fun getAxisLabel(value: Float, axis: AxisBase): String {
        if (value < 0 || value > dateArray?.size!! - 1) {
            return ""
        }
        return dateArray?.get((value).toInt())?.toStringFormat().toString()
    }
}