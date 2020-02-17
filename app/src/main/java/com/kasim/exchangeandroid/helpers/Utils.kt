package com.kasim.exchangeandroid.helpers

import android.content.Context
import java.io.IOException

class Utils {
    fun loadJSONFromAsset(context: Context, file: Int): String {
        var json: String? = null
        try {
            val iStream = context.getResources().openRawResource(file)

            val size = iStream.available()

            val buffer = ByteArray(size)

            iStream.read(buffer)

            iStream.close()

            json = String(buffer)


        } catch (ex: IOException) {
            ex.printStackTrace()
            return ""
        }

        return json
    }
}