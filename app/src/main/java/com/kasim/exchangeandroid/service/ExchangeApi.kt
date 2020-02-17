package com.kasim.exchangeandroid.service

import com.kasim.exchangeandroid.models.History
import com.kasim.exchangeandroid.models.Latest
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeApi {
    @GET("latest")
    fun getLatest(
        @Query("base") base: String
    ): Call<Latest>

    @GET("latest")
    fun getLatest(
        @Query("base") base: String,
        @Query("symbols") symbols: String
    ): Call<Latest>


    @GET("history")
    fun getHistory(
        @Query("base") base: String,
        @Query("symbols") symbols: String,
        @Query("start_at") start_at: String,
        @Query("end_at") end_at: String
    ): Call<History>
}