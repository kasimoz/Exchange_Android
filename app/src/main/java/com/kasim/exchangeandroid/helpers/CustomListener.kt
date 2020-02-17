package com.kasim.exchangeandroid.helpers


object CustomListener {
    init {
        println("Singleton class invoked.")
    }


    interface AddCurrencyChangeListener {
        fun addCurrencyChange()

        fun baseCurrencyChange()
    }

    interface SettingsChangeListener {
        fun settingsChange(code: String, name: String, symbol: String)
    }


    private var addCurrencyChangeListener: AddCurrencyChangeListener? = null

    private var settingsChangeListener: SettingsChangeListener? = null


    fun setAddCurrencyChangeListener(addCurrencyChangeListener: AddCurrencyChangeListener) {
        this.addCurrencyChangeListener = addCurrencyChangeListener
    }

    fun addCurrencyChange() {
        if (addCurrencyChangeListener != null)
            addCurrencyChangeListener!!.addCurrencyChange()
    }

    fun baseCurrencyChange() {
        if (addCurrencyChangeListener != null)
            addCurrencyChangeListener!!.baseCurrencyChange()
    }

    fun setSettingsChangeListener(settingsChangeListener: SettingsChangeListener) {
        this.settingsChangeListener = settingsChangeListener
    }

    fun settingsChange(code: String, name: String, symbol: String) {
        if (settingsChangeListener != null)
            settingsChangeListener!!.settingsChange(code, name, symbol)
    }


}