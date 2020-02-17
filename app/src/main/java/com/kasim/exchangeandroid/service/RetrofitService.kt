package com.kasim.exchangeandroid.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.exchangeratesapi.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }
}