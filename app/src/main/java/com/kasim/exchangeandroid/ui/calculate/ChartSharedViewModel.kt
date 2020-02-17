package com.kasim.exchangeandroid.ui.calculate

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kasim.exchangeandroid.helpers.SharedPreferenceManager
import com.kasim.exchangeandroid.models.History

class ChartSharedViewModel(context: Context) : ViewModel() {

    private var historyData: MutableLiveData<History>? = null

    init {
        historyData = MutableLiveData<History>()
        historyData!!.setValue(SharedPreferenceManager().getLastChart(context!!))
    }

    fun getHistoryData(): LiveData<History>? {
        return historyData
    }

    class ChartSharedViewModelFactory(val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ChartSharedViewModel(context) as T
        }
    }
}

