package com.kasim.exchangeandroid.widget


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock


class AppWidgetAlarm (context : Context){
    private val ALARM_ID = 1234

    private var mContext: Context


    init {
        mContext = context
    }


    fun startAlarm() {

        val alarmIntent = Intent(mContext, LatestWidget::class.java)
        alarmIntent.action = LatestWidget().ACTION_AUTO_UPDATE
        val pendingIntent = PendingIntent.getBroadcast(
            mContext,
            ALARM_ID,
            alarmIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )


        val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager?.setRepeating(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + 1000,
            AlarmManager.INTERVAL_FIFTEEN_MINUTES,
            pendingIntent)
    }


    fun stopAlarm() {
        val alarmIntent = Intent(LatestWidget().ACTION_AUTO_UPDATE)
        val pendingIntent = PendingIntent.getBroadcast(
            mContext,
            ALARM_ID,
            alarmIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}