package com.kasim.exchangeandroid.ui.latest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kasim.exchangeandroid.models.Latest
import com.kasim.exchangeandroid.service.ExchangeRepository

class LatestViewModel : ViewModel() {

    private var mutableLiveData: MutableLiveData<Latest>? = null
    private var exchangeRepository: ExchangeRepository? = null


    fun init(base: String) {
        mutableLiveData = null
        exchangeRepository = ExchangeRepository
        mutableLiveData = exchangeRepository!!.getLatest(base)
    }

    fun getLatestRepository(): LiveData<Latest>? {
        return mutableLiveData
    }

}