package com.kasim.exchangeandroid.models

class Settings {

    var list: ArrayList<Item> = ArrayList()

    class Item {
        lateinit var image: String
        lateinit var title: String
        lateinit var value: String
        var header: Boolean = false

    }
}