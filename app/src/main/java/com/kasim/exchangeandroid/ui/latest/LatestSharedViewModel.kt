package com.kasim.exchangeandroid.ui.latest

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kasim.exchangeandroid.helpers.SharedPreferenceManager
import com.kasim.exchangeandroid.models.Latest

class LatestSharedViewModel(context: Context) : ViewModel() {

    private var latestData: MutableLiveData<Latest>? = null

    init {
        latestData = MutableLiveData<Latest>()
        latestData!!.setValue(SharedPreferenceManager().getLatest(context!!))
    }

    fun getWeatherData(): LiveData<Latest>? {
        return latestData
    }

    class LatestSharedViewModelFactory(val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LatestSharedViewModel(context) as T
        }
    }
}

