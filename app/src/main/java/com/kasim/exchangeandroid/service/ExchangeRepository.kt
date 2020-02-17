package com.kasim.exchangeandroid.service

import androidx.lifecycle.MutableLiveData
import com.kasim.exchangeandroid.models.History
import com.kasim.exchangeandroid.models.Latest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object ExchangeRepository {
    private var exchangeApi: ExchangeApi? = null

    init {
        exchangeApi = RetrofitService().createService(ExchangeApi::class.java)
    }

    fun getLatest(base: String): MutableLiveData<Latest> {
        val latestData = MutableLiveData<Latest>()
        exchangeApi?.getLatest(base)?.enqueue(object : Callback<Latest> {
            override fun onResponse(call: Call<Latest>, response: Response<Latest>) {
                if (response.isSuccessful()) {
                    latestData.setValue(response.body())
                } else {
                    latestData.setValue(null)
                }
            }

            override fun onFailure(call: Call<Latest>, t: Throwable) {
                latestData.setValue(null)
            }
        })
        return latestData
    }

    fun getLatestWithSymbols(base: String, symbols: String): MutableLiveData<Latest> {
        val latestData = MutableLiveData<Latest>()
        exchangeApi?.getLatest(base, symbols)?.enqueue(object : Callback<Latest> {
            override fun onResponse(call: Call<Latest>, response: Response<Latest>) {
                if (response.isSuccessful()) {
                    latestData.setValue(response.body())
                } else {
                    latestData.setValue(null)
                }
            }

            override fun onFailure(call: Call<Latest>, t: Throwable) {
                latestData.setValue(null)
            }
        })
        return latestData
    }


    fun getHistory(
        base: String,
        symbols: String,
        start_at: String,
        end_at: String
    ): MutableLiveData<History> {
        val historyData = MutableLiveData<History>()
        exchangeApi?.getHistory(base, symbols, start_at, end_at)
            ?.enqueue(object : Callback<History> {
                override fun onResponse(call: Call<History>, response: Response<History>) {
                    if (response.isSuccessful()) {
                        historyData.setValue(response.body())
                    } else {
                        historyData.setValue(null)
                    }
                }

                override fun onFailure(call: Call<History>, t: Throwable) {
                    historyData.setValue(null)
                }
            })
        return historyData
    }

}


