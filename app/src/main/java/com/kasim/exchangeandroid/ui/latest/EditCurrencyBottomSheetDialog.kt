package com.kasim.exchangeandroid.ui.latest

import android.app.Dialog
import android.view.LayoutInflater
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kasim.exchangeandroid.adapters.EditCurrencyAdapter
import com.kasim.exchangeandroid.helpers.CustomListener
import com.kasim.exchangeandroid.helpers.RecyclerViewDivider
import com.kasim.exchangeandroid.helpers.SharedPreferenceManager


class EditCurrencyBottomSheetDialog : BottomSheetDialogFragment(),
    EditCurrencyAdapter.OnItemClickListener {

    lateinit var editCurrencyAdapter: EditCurrencyAdapter
    var keys = ArrayList<String>()


    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        keys = SharedPreferenceManager().getCurrenciesArray(context!!)
        val view = LayoutInflater.from(context)
            .inflate(com.kasim.exchangeandroid.R.layout.edit_currency, null)
        editCurrencyAdapter = EditCurrencyAdapter(this.context!!, keys, this)
        val recyclerView: RecyclerView =
            view.findViewById(com.kasim.exchangeandroid.R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = editCurrencyAdapter
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(RecyclerViewDivider(activity!!))
        dialog.setContentView(view)
    }

    override fun onItemClick(selected: String) {
        if (keys.contains(selected)) {
            keys.remove(selected)
        } else {
            keys.add(selected)
        }
        SharedPreferenceManager().setCurrenciesArray(context!!, keys)
        editCurrencyAdapter.notifyDataSetChanged()
        CustomListener.addCurrencyChange()
    }
}

