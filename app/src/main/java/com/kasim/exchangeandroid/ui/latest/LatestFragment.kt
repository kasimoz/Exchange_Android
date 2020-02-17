package com.kasim.exchangeandroid.ui.latest

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kasim.exchangeandroid.adapters.LatestAdapter
import com.kasim.exchangeandroid.helpers.*
import com.kasim.exchangeandroid.models.Latest
import com.kasim.exchangeandroid.ui.BaseCurrencyBottomSheetDialog
import java.util.*
import kotlin.collections.ArrayList


class LatestFragment : Fragment(), Toolbar.OnMenuItemClickListener,
    CustomListener.AddCurrencyChangeListener {

    private lateinit var latestSharedViewModel: LatestSharedViewModel
    private lateinit var latestViewModel: LatestViewModel
    private lateinit var mainLates: Latest
    private var baseFlag: ImageView? = null
    private var baseCode: TextView? = null
    private var baseName: TextView? = null
    private var baseMoney: TextView? = null
    private lateinit var recyclerView: RecyclerView
    var keys: ArrayList<String> = ArrayList<String>()
    var latestAdapter: LatestAdapter? = null
    private var lastClickTime: Long = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        CustomListener.setAddCurrencyChangeListener(this)
        latestSharedViewModel = ViewModelProvider(
            this,
            LatestSharedViewModel.LatestSharedViewModelFactory(context!!)
        ).get(LatestSharedViewModel::class.java)
        latestViewModel = ViewModelProvider(this).get(LatestViewModel::class.java)
        val root =
            inflater.inflate(com.kasim.exchangeandroid.R.layout.fragment_latest, container, false)
        latestSharedViewModel.getWeatherData()?.observe(this, Observer {
            if (it != null) {
                mainLates = it
                setupRecyclerView()
            }
        })

        baseFlag = root.findViewById(com.kasim.exchangeandroid.R.id.flag)
        baseCode = root.findViewById(com.kasim.exchangeandroid.R.id.code)
        baseName = root.findViewById(com.kasim.exchangeandroid.R.id.name)
        baseMoney = root.findViewById(com.kasim.exchangeandroid.R.id.money)
        recyclerView = root.findViewById(com.kasim.exchangeandroid.R.id.recyclerView)

        keys = SharedPreferenceManager().getCurrenciesArray(context!!)

        val toolbar: Toolbar = root.findViewById(com.kasim.exchangeandroid.R.id.toolbar)
        toolbar.inflateMenu(com.kasim.exchangeandroid.R.menu.latest_menu)
        toolbar.setOnMenuItemClickListener(this)

        val title = toolbar.getTitle()
        for (i in 0 until toolbar.getChildCount()) {
            val tmpView = toolbar.getChildAt(i)
            if ("androidx.appcompat.widget.AppCompatTextView" == tmpView.javaClass.getName()) {
                if ((tmpView as AppCompatTextView).text == title) {
                    tmpView.setOnClickListener(View.OnClickListener {
                        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                            return@OnClickListener
                        }
                        lastClickTime = SystemClock.elapsedRealtime();
                        val fragment = BaseCurrencyBottomSheetDialog()
                        fragment.show(activity!!.supportFragmentManager, "TAG1")
                    })
                }
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Date().time - SharedPreferenceManager().getLatestRequestTime(context!!) > 0 && Date().time - SharedPreferenceManager().getLatestRequestTime(
                context!!
            ) < 30 * 60 * 1000
        ) {
            return
        }
        lastestService()
    }

    override fun onResume() {
        super.onResume()
        setBaseCurrencyVies()
    }

    private fun lastestService() {
        val base = SharedPreferenceManager().getBaseCurrency(context!!)
        latestViewModel.init(base.get(0))
        latestViewModel.getLatestRepository()?.observe(this, Observer {
            if (it != null) {
                SharedPreferenceManager().setLatestRequestTime(context!!, Date().time)
                SharedPreferenceManager().setLatest(context!!, it)
                mainLates = it
                setupRecyclerView()
            }
        })
    }

    private fun setupRecyclerView() {
        if (latestAdapter == null) {
            latestAdapter = LatestAdapter(this.context!!, mainLates, keys)
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.adapter = latestAdapter
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.addItemDecoration(RecyclerViewDivider(activity!!))
        } else {
            latestAdapter?.latest = mainLates
            latestAdapter?.notifyDataSetChanged()
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return true
        }
        lastClickTime = SystemClock.elapsedRealtime();
        val fragment = EditCurrencyBottomSheetDialog()
        fragment.show(activity!!.supportFragmentManager, "TAG2")
        return true
    }


    override fun addCurrencyChange() {
        keys.clear()
        keys.addAll(SharedPreferenceManager().getCurrenciesArray(context!!))
        latestAdapter!!.notifyDataSetChanged()
    }

    override fun baseCurrencyChange() {
        setBaseCurrencyVies()
        lastestService()
    }

    fun setBaseCurrencyVies() {
        val currencies = context!!.getCurrencies()
        val baseArray = SharedPreferenceManager().getBaseCurrency(context!!)
        val values = currencies[baseArray.get(0)] as Map<String, String>
        baseFlag?.setImageResource(values["Image"]?.getDrawable(context!!)!!)
        baseCode?.text = baseArray.get(0)
        baseName?.text = baseArray.get(1)
        baseMoney?.text = baseArray.get(2) + "1"
    }


}


