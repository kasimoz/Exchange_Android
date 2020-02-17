package com.kasim.exchangeandroid.ui.settings

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kasim.exchangeandroid.R
import com.kasim.exchangeandroid.adapters.SettingsAdapter
import com.kasim.exchangeandroid.helpers.CustomListener
import com.kasim.exchangeandroid.helpers.RecyclerViewDivider
import com.kasim.exchangeandroid.helpers.SharedPreferenceManager
import com.kasim.exchangeandroid.helpers.getSettings
import com.kasim.exchangeandroid.ui.SettingsCurrencyBottomSheetDialog

class SettingsFragment : Fragment(), SettingsAdapter.OnItemClickListener {

    var settingsAdapter: SettingsAdapter? = null
    private var lastClickTime: Long = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        val settings = context?.getSettings()
        settingsAdapter = SettingsAdapter(this.context!!, settings!!, this)
        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = settingsAdapter
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(RecyclerViewDivider(activity!!))
        return root
    }

    override fun onItemClick(selected: String) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime();
        val fragment = SettingsCurrencyBottomSheetDialog()
        fragment.show(activity!!.supportFragmentManager, "TAG1")
        CustomListener.setSettingsChangeListener(object :
            CustomListener.SettingsChangeListener {
            override fun settingsChange(code: String, name: String, symbol: String) {
                when (selected) {
                    "Currency" -> SharedPreferenceManager().setBaseCurrency(
                        context!!,
                        code,
                        name,
                        symbol
                    )
                    "From" -> SharedPreferenceManager().setDefaultFrom(context!!, code)
                    "To" -> SharedPreferenceManager().setDefaultTo(context!!, code)
                    "Widget Base" -> SharedPreferenceManager().setWidgetBase(context!!, code)
                    "Widget First" -> SharedPreferenceManager().setWidgetFirst(context!!, code)
                    "Widget Second" -> SharedPreferenceManager().setWidgetSecond(context!!, code)
                    else -> print("else")
                }
                settingsAdapter?.notifyDataSetChanged()
            }
        })
    }
}