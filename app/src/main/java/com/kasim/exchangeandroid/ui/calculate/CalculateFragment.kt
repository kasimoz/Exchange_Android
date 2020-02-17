package com.kasim.exchangeandroid.ui.calculate

import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.kasim.exchangeandroid.R
import com.kasim.exchangeandroid.chartFormatter.DateChartFormatter
import com.kasim.exchangeandroid.helpers.*
import com.kasim.exchangeandroid.models.History
import com.kasim.exchangeandroid.ui.SettingsCurrencyBottomSheetDialog
import java.util.*
import kotlin.collections.ArrayList


class CalculateFragment : Fragment(), View.OnClickListener, View.OnFocusChangeListener,
    ChipGroup.OnCheckedChangeListener {

    private lateinit var calculateViewModel: CalculateViewModel
    private lateinit var chartViewModel: ChartViewModel
    private lateinit var chartSharedViewModel: ChartSharedViewModel
    private var chart: LineChart? = null
    private var fromIcon: ImageView? = null
    private var toIcon: ImageView? = null
    private var amount: EditText? = null
    private var result: EditText? = null
    private var fromText: TextView? = null
    private var toText: TextView? = null
    private var exchange: ImageButton? = null
    private var chartChipGroup: ChipGroup? = null
    private var lastWeek: Chip? = null
    private lateinit var mainHistory: History
    private var yearArray = ArrayList<String>()
    private var monthArray = ArrayList<String>()
    private var weekArray = ArrayList<String>()
    private var lastClickTime: Long = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_calculate, container, false)
        chartSharedViewModel = ViewModelProvider(
            this,
            ChartSharedViewModel.ChartSharedViewModelFactory(context!!)
        ).get(ChartSharedViewModel::class.java)
        calculateViewModel = ViewModelProvider(this).get(CalculateViewModel::class.java)
        chartViewModel = ViewModelProvider(this).get(ChartViewModel::class.java)
        fromIcon = root.findViewById(R.id.fromIcon)
        toIcon = root.findViewById(R.id.toIcon)
        amount = root.findViewById(R.id.amount)
        result = root.findViewById(R.id.result)
        fromText = root.findViewById(R.id.fromText)
        toText = root.findViewById(R.id.toText)
        exchange = root.findViewById(R.id.exchange)
        lastWeek = root.findViewById(R.id.lastWeek)
        chartChipGroup = root.findViewById(R.id.chartChipGroup)
        exchange?.setOnClickListener(this)
        fromIcon?.setOnClickListener(this)
        toIcon?.setOnClickListener(this)
        amount?.setOnFocusChangeListener(this)
        chartChipGroup?.setOnCheckedChangeListener(this)

        chart = root.findViewById(R.id.chart1)
        chart?.setViewPortOffsets(0F, 64F, 0F, 0F)

        // no description text
        chart?.getDescription()!!.isEnabled = false

        // enable touch gestures
        chart?.setTouchEnabled(true)

        // enable scaling and dragging
        chart?.setDragEnabled(true)
        chart?.setScaleEnabled(true)

        // if disabled, scaling can be done on x- and y-axis separately
        chart?.setPinchZoom(false)

        chart?.setDrawGridBackground(false)
        chart?.setMaxHighlightDistance(300F)

        val x = chart?.getXAxis()
        x?.isEnabled = true
        x?.setDrawGridLines(true)
        x?.setLabelCount(6, false)
        x?.textColor = Color.BLACK
        x?.axisLineColor = Color.BLACK

        val y = chart?.getAxisLeft()
        y?.setLabelCount(6, false)
        y?.textColor = Color.BLACK
        y?.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        y?.setDrawGridLines(true)

        y?.axisLineColor = Color.BLACK


        chart?.getAxisRight()!!.isEnabled = false


        chart?.getLegend()!!.isEnabled = false

        chart?.animateXY(2000, 2000)



        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        chartSharedViewModel.getHistoryData()?.observe(this, Observer {
            if (it != null) {
                if (!it.end_at.equals(Date().toStringFormat())) {
                    chartHistoryService()
                } else {
                    val map =
                        it.rates?.get(it.rates?.keys?.first().toString()) as? Map<String, Double>
                    val to = map?.keys?.first().toString()
                    if (!it.base.equals(SharedPreferenceManager().getDefaultFrom(context!!)) || !to.equals(
                            SharedPreferenceManager().getDefaultTo(context!!)
                        )
                    ) {
                        chartHistoryService()
                    } else {
                        mainHistory = it
                        reloadChartView()
                    }
                }

            } else {
                chartHistoryService()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        setFromToCurrencyVies()
    }

    override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime();
        when (v?.id) {
            R.id.exchange -> exchangeAction()
            R.id.fromIcon -> fromIconAction()
            R.id.toIcon -> toIconAction()
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            amount?.setText("")
            result?.setText("")
        }
    }

    fun chartHistoryService() {
        val default = SharedPreferenceManager().getDefaultFrom(context!!)
        val to = SharedPreferenceManager().getDefaultTo(context!!)
        val start_at = Date().addingYear(value = -1)
        val end_at = Date().toStringFormat()

        chartViewModel.init(default, to, start_at, end_at)
        chartViewModel.getHistoryRepository()?.observe(this, Observer {
            if (it != null) {
                SharedPreferenceManager().setLastChart(context!!, it)
                mainHistory = it
                reloadChartView()
            }
        })
    }

    fun exchangeAction() {
        amount?.hideKeyboard()
        if (amount!!.text.isEmpty())
            return

        val default = SharedPreferenceManager().getDefaultFrom(context!!)
        val to = SharedPreferenceManager().getDefaultTo(context!!)

        calculateViewModel.init(default, to)
        calculateViewModel.getLatestRepository()?.observe(this, Observer {
            if (it != null) {
                if ((amount!!.text.toString().toDouble()) != null && it.rates!![to].toString().toDouble() != null) {
                    val resultDouble =
                        (amount!!.text.toString().toDouble()) * (it.rates!![to].toString().toDouble())
                    val text = "%.3f".format(resultDouble)
                    result?.setText(text)
                }
            }
        })
    }

    fun fromIconAction() {
        val fragment = SettingsCurrencyBottomSheetDialog()
        fragment.show(activity!!.supportFragmentManager, "TAG1")
        CustomListener.setSettingsChangeListener(object :
            CustomListener.SettingsChangeListener {
            override fun settingsChange(code: String, name: String, symbol: String) {
                if (!code.equals(SharedPreferenceManager().getDefaultFrom(context!!))) {
                    SharedPreferenceManager().setDefaultFrom(context!!, code)
                    chartHistoryService()
                }
                SharedPreferenceManager().setDefaultFrom(context!!, code)
                setFromToCurrencyVies()
                amount?.setText("")
                result?.setText("")
            }
        })
    }

    fun toIconAction() {
        val fragment = SettingsCurrencyBottomSheetDialog()
        fragment.show(activity!!.supportFragmentManager, "TAG2")
        CustomListener.setSettingsChangeListener(object :
            CustomListener.SettingsChangeListener {
            override fun settingsChange(code: String, name: String, symbol: String) {
                if (!code.equals(SharedPreferenceManager().getDefaultTo(context!!))) {
                    SharedPreferenceManager().setDefaultTo(context!!, code)
                    chartHistoryService()
                }
                SharedPreferenceManager().setDefaultTo(context!!, code)
                setFromToCurrencyVies()
                amount?.setText("")
                result?.setText("")
            }
        })
    }


    fun setFromToCurrencyVies() {
        val currencies = context!!.getCurrencies()
        val valuesFrom =
            currencies[SharedPreferenceManager().getDefaultFrom(context!!)] as Map<String, String>
        val valuesTo =
            currencies[SharedPreferenceManager().getDefaultTo(context!!)] as Map<String, String>
        fromIcon?.setImageResource(valuesFrom["Image"]?.getDrawable(context!!)!!)
        fromText?.text =
            SharedPreferenceManager().getDefaultFrom(context!!) + " - " + valuesFrom["Symbol"]
        toIcon?.setImageResource(valuesTo["Image"]?.getDrawable(context!!)!!)
        toText?.text =
            SharedPreferenceManager().getDefaultTo(context!!) + " - " + valuesTo["Symbol"]
    }

    private fun reloadChartView() {
        yearArray = mainHistory.rates?.getKeys()!!
        weekArray = yearArray?.suffix(7)
        monthArray = yearArray?.suffix(30)
        setData(weekArray!!)
        chart?.invalidate()
        lastWeek?.isCheckable = true
    }


    /**
     * set chart data
     *
     * @param array date array
     */
    private fun setData(array: ArrayList<String>) {

        val values = ArrayList<Entry>()

        for (i in 0 until array.size) {
            val map = mainHistory.rates?.get(array.get(i)) as? Map<String, Double>
            val value = if (map?.get(SharedPreferenceManager().getDefaultTo(context!!)) != null) {
                map?.get(SharedPreferenceManager().getDefaultTo(context!!))
            } else {
                0.0
            }
            values.add(Entry(i.toFloat(), value?.toFloat()!!))
        }

        val set1: LineDataSet

        val x = chart?.getXAxis()
        x?.spaceMax = (array.size / 7).toFloat()
        x?.spaceMin = (array.size / 7).toFloat()
        x?.valueFormatter = DateChartFormatter(array)

        if (chart?.getData() != null && chart!!.getData().dataSetCount > 0) {
            set1 = chart?.getData()!!.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            chart?.getData()!!.notifyDataChanged()
            chart?.notifyDataSetChanged()
        } else {

            set1 = LineDataSet(values, "Exchange Chart")

            set1.mode = LineDataSet.Mode.CUBIC_BEZIER
            set1.cubicIntensity = 0.2f
            set1.setDrawFilled(true)
            set1.setDrawCircles(false)
            set1.lineWidth = 1.8f
            set1.circleRadius = 4f
            set1.setCircleColor(Color.BLACK)
            set1.color = ContextCompat.getColor(context!!, R.color.colorRed)
            set1.fillColor = ContextCompat.getColor(context!!, R.color.colorRed)
            set1.fillAlpha = 0
            set1.setDrawHorizontalHighlightIndicator(false)
            set1.fillFormatter =
                IFillFormatter { dataSet, dataProvider -> chart?.getAxisLeft()!!.axisMinimum }

            // create a data object with the data sets
            val data = LineData(set1)
            data.setValueTextSize(9f)
            data.setValueTextColor(Color.BLACK)
            data.setDrawValues(true)

            // set data
            chart?.setData(data)
        }
    }

    override fun onCheckedChanged(group: ChipGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.lastWeek -> setData(weekArray)
            R.id.lastMonth -> setData(monthArray)
            R.id.lastYear -> setData(yearArray)
        }
        chart?.invalidate()
    }
}