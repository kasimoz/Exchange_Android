package com.kasim.exchangeandroid.ui

import android.app.Dialog
import android.view.LayoutInflater
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kasim.exchangeandroid.adapters.BaseCurrencyAdapter
import com.kasim.exchangeandroid.helpers.CustomListener
import com.kasim.exchangeandroid.helpers.RecyclerViewDivider
import com.kasim.exchangeandroid.helpers.SharedPreferenceManager
import com.kasim.exchangeandroid.helpers.getCurrencies

class SettingsCurrencyBottomSheetDialog : BottomSheetDialogFragment(),
    BaseCurrencyAdapter.OnItemClickListener {

    lateinit var baseCurrencyAdapter: BaseCurrencyAdapter
    var keys = ArrayList<String>()

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        keys = SharedPreferenceManager().getCurrenciesArray(context!!)
        val view = LayoutInflater.from(context)
            .inflate(com.kasim.exchangeandroid.R.layout.settings_currency, null)
        baseCurrencyAdapter = BaseCurrencyAdapter(this.context!!, this)
        val recyclerView: RecyclerView =
            view.findViewById(com.kasim.exchangeandroid.R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = baseCurrencyAdapter
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(RecyclerViewDivider(activity!!))
        dialog.setContentView(view)
    }

    override fun onItemClick(selected: String) {
        val currencies = context!!.getCurrencies()
        val values = currencies[selected] as Map<String, String>
        CustomListener.settingsChange(
            selected,
            values["Name"].toString(),
            values["Symbol"].toString()
        )
        dismiss()
    }
}
