package com.kasim.exchangeandroid.ui.calculate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kasim.exchangeandroid.models.History
import com.kasim.exchangeandroid.service.ExchangeRepository

class ChartViewModel : ViewModel() {

    private var mutableLiveData: MutableLiveData<History>? = null
    private var exchangeRepository: ExchangeRepository? = null


    fun init(base: String, symbols: String, start_at: String, end_at: String) {
        mutableLiveData = null
        exchangeRepository = ExchangeRepository
        mutableLiveData = exchangeRepository!!.getHistory(base, symbols, start_at, end_at)
    }

    fun getHistoryRepository(): LiveData<History>? {
        return mutableLiveData
    }
}