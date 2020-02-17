package com.kasim.exchangeandroid.widget

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters


class HourlyWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    var context : Context? = null

    init {
        context = ctx
    }

    override fun doWork(): Result {
        val alarmIntent = Intent(context, LatestWidget::class.java)
        alarmIntent.action = LatestWidget().ACTION_AUTO_UPDATE
        context?.sendBroadcast(alarmIntent)
        return Result.success()
    }

}