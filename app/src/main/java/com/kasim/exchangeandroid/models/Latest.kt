package com.kasim.exchangeandroid.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Latest {

    @SerializedName("rates")
    @Expose
    var rates: Map<String, Double>? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("base")
    @Expose
    var base: String? = null

}