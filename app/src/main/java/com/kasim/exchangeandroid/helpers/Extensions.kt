package com.kasim.exchangeandroid.helpers

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kasim.exchangeandroid.models.Currencies
import com.kasim.exchangeandroid.models.Settings
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


fun RecyclerView.lastVisibleItemPosition(): Int {
    val layoutManager = this.getLayoutManager()
    return if (layoutManager is LinearLayoutManager) {
        (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
    } else 0
}

fun Context.getCurrencies(): Map<String, Any> {
    val jsonString = Utils().loadJSONFromAsset(this, com.kasim.exchangeandroid.R.raw.currencies)
    val currencies = Gson().fromJson(jsonString, Currencies::class.java)
    return currencies.list
}

fun Context.getSettings(): Settings {
    val jsonString = Utils().loadJSONFromAsset(this, com.kasim.exchangeandroid.R.raw.settings)
    val settings = Gson().fromJson(jsonString, Settings::class.java)
    return settings
}

fun Map<String, Any>.getKeys(): ArrayList<String> {
    val list = ArrayList<String>(this.keys)
    Collections.sort(list)
    return list
}

fun String.getDrawable(context: Context): Int {
    return context.getResources().getIdentifier(this, "drawable", context!!.getPackageName())
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
    this.rootView.requestFocus()
}


fun Date.addingYear(format: String = "yyyy-MM-dd", value: Int): String {
    var date = ""
    val c = Calendar.getInstance()
    c.time = this
    c.add(Calendar.YEAR, value)
    try {
        val formatter = SimpleDateFormat(format)
        date = formatter.format(c.time)
    } catch (e: Exception) {
        Log.e("formattedDateFromString", "Exception in formateDateFromstring(): " + e.message)
    }

    return date
}

fun Date.toStringFormat(format: String = "yyyy-MM-dd"): String {
    var date = ""
    try {
        val formatter = SimpleDateFormat(format)
        date = formatter.format(this)
    } catch (e: Exception) {
        Log.e("formattedDateFromString", "Exception in formateDateFromstring(): " + e.message)
    }

    return date
}

fun String.toStringFormat(format: String = "dd MMM"): String {
    var time = this
    val inputFormat = "yyyy-MM-dd"
    var parsed: Date? = null
    val df_input = SimpleDateFormat(inputFormat, Locale.getDefault())

    try {
        parsed = df_input.parse(this)
        val formatter = SimpleDateFormat(format)
        time = formatter.format(parsed!!.time)
    } catch (e: Exception) {
        Log.e("formattedDateFromString", "Exception in formateDateFromstring(): " + e.message)
    }

    return time
}

fun ArrayList<String>.suffix(length: Int): ArrayList<String> {
    val list = this.subList(this.size - length, this.size)
    return list.toList() as ArrayList<String>
}