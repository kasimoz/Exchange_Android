package com.kasim.exchangeandroid.helpers

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kasim.exchangeandroid.models.History
import com.kasim.exchangeandroid.models.Latest
import java.lang.reflect.Modifier
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class SharedPreferenceManager {
    fun getPrivatePreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("ExchangeApp", Context.MODE_PRIVATE)
    }

    fun setCurrenciesArray(context: Context, array: ArrayList<String>) {
        val list = HashSet<String>(array)
        getPrivatePreferences(context).edit()
            .putStringSet("CurrenciesArray", list)
            .apply()
    }

    fun getCurrenciesArray(context: Context): ArrayList<String> {
        val keys = getPrivatePreferences(context).getStringSet(
            "CurrenciesArray",
            context.getCurrencies().keys
        )
        val list = ArrayList<String>(keys!!)
        Collections.sort(list)
        return list
    }


    fun setWidgetBase(context: Context, value: String) {
        getPrivatePreferences(context).edit()
            .putString("WidgetBase", value)
            .apply()
    }

    fun getWidgetBase(context: Context): String {
        return getPrivatePreferences(context).getString("WidgetBase", "USD").toString()
    }

    fun setWidgetFirst(context: Context, value: String) {
        getPrivatePreferences(context).edit()
            .putString("WidgetFirst", value)
            .apply()
    }

    fun getWidgetFirst(context: Context): String {
        return getPrivatePreferences(context).getString("WidgetFirst", "TRY").toString()
    }

    fun setWidgetSecond(context: Context, value: String) {
        getPrivatePreferences(context).edit()
            .putString("WidgetSecond", value)
            .apply()
    }

    fun getWidgetSecond(context: Context): String {
        return getPrivatePreferences(context).getString("WidgetSecond", "EUR").toString()
    }

    fun setBaseCurrency(context: Context, code: String, name: String, symbol: String) {
        getPrivatePreferences(context).edit()
            .putString("BaseCode", code)
            .putString("BaseName", name)
            .putString("BaseSymbol", symbol)
            .apply()
    }

    fun getBaseCurrency(context: Context): Array<String> {
        var default: Array<String> = arrayOf("USD", "United States Dollar", "$")
        default[0] = getPrivatePreferences(context).getString("BaseCode", "USD").toString()
        default[1] =
            getPrivatePreferences(context).getString("BaseName", "United States Dollar").toString()
        default[2] = getPrivatePreferences(context).getString("BaseSymbol", "$").toString()
        return default
    }

    fun setDefaultFrom(context: Context, value: String) {
        getPrivatePreferences(context).edit()
            .putString("From", value)
            .apply()
    }

    fun getDefaultFrom(context: Context): String {
        return getPrivatePreferences(context).getString("From", "USD").toString()
    }

    fun setDefaultTo(context: Context, value: String) {
        getPrivatePreferences(context).edit()
            .putString("To", value)
            .apply()
    }

    fun getDefaultTo(context: Context): String {
        return getPrivatePreferences(context).getString("To", "TRY").toString()
    }

    fun setLatestRequestTime(context: Context, value: Long) {
        getPrivatePreferences(context).edit()
            .putLong("LatestRequestTime", value)
            .apply()
    }

    fun getLatestRequestTime(context: Context): Long {
        return getPrivatePreferences(context).getLong("LatestRequestTime", Date().time)
    }

    fun setLatest(context: Context, latest: Latest) {
        val gson = GsonBuilder().excludeFieldsWithModifiers(
            Modifier.FINAL,
            Modifier.TRANSIENT,
            Modifier.STATIC
        )
            .serializeNulls()
            .create()
        getPrivatePreferences(context).edit()
            .putString("Latest", gson.toJson(latest))
            .apply()
    }

    fun getLatest(context: Context): Latest? {
        val gson = Gson()
        return if (getPrivatePreferences(context).getString("Latest", "") == "") {
            null
        } else {
            gson.fromJson(
                getPrivatePreferences(context).getString("Latest", ""),
                Latest::class.java
            )
        }
    }

    fun setLastChart(context: Context, history: History) {
        val gson = GsonBuilder().excludeFieldsWithModifiers(
            Modifier.FINAL,
            Modifier.TRANSIENT,
            Modifier.STATIC
        )
            .serializeNulls()
            .create()
        getPrivatePreferences(context).edit()
            .putString("History", gson.toJson(history))
            .apply()
    }

    fun getLastChart(context: Context): History? {
        val gson = Gson()
        return if (getPrivatePreferences(context).getString("History", "") == "") {
            null
        } else {
            gson.fromJson(
                getPrivatePreferences(context).getString("History", ""), History::class.java
            )
        }
    }
}