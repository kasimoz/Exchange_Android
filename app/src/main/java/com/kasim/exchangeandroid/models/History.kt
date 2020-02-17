package com.kasim.exchangeandroid.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class History {

    @SerializedName("rates")
    @Expose
    var rates: Map<String, Any>? = null
    @SerializedName("start_at")
    @Expose
    var start_at: String? = null
    @SerializedName("end_at")
    @Expose
    var end_at: String? = null
    @SerializedName("base")
    @Expose
    var base: String? = null

}