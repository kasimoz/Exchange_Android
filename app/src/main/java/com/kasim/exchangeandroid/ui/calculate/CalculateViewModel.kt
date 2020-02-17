package com.kasim.exchangeandroid.ui.calculate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kasim.exchangeandroid.models.Latest
import com.kasim.exchangeandroid.service.ExchangeRepository

class CalculateViewModel : ViewModel() {

    private var mutableLiveData: MutableLiveData<Latest>? = null
    private var exchangeRepository: ExchangeRepository? = null


    fun init(base: String, symbols: String) {
        mutableLiveData = null
        exchangeRepository = ExchangeRepository
        mutableLiveData = exchangeRepository!!.getLatestWithSymbols(base, symbols)

    }

    fun getLatestRepository(): LiveData<Latest>? {
        return mutableLiveData
    }

}