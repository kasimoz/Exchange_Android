package com.kasim.exchangeandroid.widget

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.kasim.exchangeandroid.MainActivity
import com.kasim.exchangeandroid.R
import com.kasim.exchangeandroid.helpers.SharedPreferenceManager
import com.kasim.exchangeandroid.helpers.getCurrencies
import com.kasim.exchangeandroid.helpers.getDrawable
import com.kasim.exchangeandroid.models.Latest
import com.kasim.exchangeandroid.service.ExchangeApi
import com.kasim.exchangeandroid.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit


/**
 * Implementation of App Widget functionality.
 */
class LatestWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, false)
        }
    }

    val ACTION_AUTO_UPDATE : String = "AUTO_UPDATE";

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if(intent?.action.equals(ACTION_AUTO_UPDATE))
        {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisAppWidgetComponentName = ComponentName(context!!.getPackageName(), javaClass.name)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName)
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId, true)
            }
        }
    }

    val constraints = Constraints.Builder()
        .setRequiresDeviceIdle(false)
        .setRequiresCharging(false)
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(false)
        .setRequiresStorageNotLow(false)
        .build()

    override fun onEnabled(context: Context) {
        // If you want, you can use alarmmanager instead of workmanager
      //  val appWidgetAlarm = AppWidgetAlarm(context)
      //  appWidgetAlarm.startAlarm()
        val periodicWorkRequest = PeriodicWorkRequest.Builder(HourlyWorker::class.java, 15, TimeUnit.MINUTES).setConstraints(constraints).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork("HourlyWorker", ExistingPeriodicWorkPolicy.REPLACE,periodicWorkRequest)
    }

    override fun onDisabled(context: Context) {
        // Stop alarmmanager
       /* val appWidgetManager = AppWidgetManager.getInstance(context);
        val thisAppWidgetComponentName = ComponentName(context.getPackageName(), javaClass.name);
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName);
        if (appWidgetIds.size == 0) {
            val appWidgetAlarm = AppWidgetAlarm(context);
            appWidgetAlarm.stopAlarm()
        }*/
        WorkManager.getInstance(context).cancelAllWork()
    }

    companion object {

        val NOTIFICATION_ID = "exchange_notification_id"
        val NOTIFICATION_NAME = "Widget"
        val NOTIFICATION_CHANNEL = "exchange_channel_widget"

        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int, showNotification : Boolean
        ) {


            val views = RemoteViews(context.packageName, R.layout.latest_widget)
            val currencies= context.getCurrencies()
            val baseMap = currencies.get(SharedPreferenceManager().getWidgetBase(context)) as? Map<String,String>
            val firstMap = currencies.get(SharedPreferenceManager().getWidgetFirst(context)) as? Map<String,String>
            val secondMap = currencies.get(SharedPreferenceManager().getWidgetSecond(context)) as? Map<String,String>
            views.setImageViewResource(R.id.baseFlag, baseMap?.get("Image")?.getDrawable(context)!!)
            views.setTextViewText(R.id.base, baseMap.get("Symbol")+"1")
            views.setImageViewResource(R.id.firstFlag, firstMap?.get("Image")?.getDrawable(context)!!)
            views.setImageViewResource(R.id.secondFlag, secondMap?.get("Image")?.getDrawable(context)!!)
            val exchangeApi = RetrofitService().createService(ExchangeApi::class.java)
            exchangeApi?.getLatest(SharedPreferenceManager().getWidgetBase(context), SharedPreferenceManager().getWidgetFirst(context) + "," + SharedPreferenceManager().getWidgetSecond(context))?.enqueue(object :
                Callback<Latest> {
                override fun onResponse(call: Call<Latest>, response: Response<Latest>) {
                    if (response.isSuccessful()) {
                        views.setTextViewText(R.id.first, "%s%.3f".format(firstMap.get("Symbol"),response.body()?.rates?.get(SharedPreferenceManager().getWidgetFirst(context)) ?: 0.0))
                        views.setTextViewText(R.id.second, "%s%.3f".format(secondMap.get("Symbol"),response.body()?.rates?.get(SharedPreferenceManager().getWidgetSecond(context)) ?: 0.0))
                        //Show notification when widget update
                        //if (showNotification)
                        //sendNotification(context,777,baseMap.get("Symbol")+"1","%s%.3f".format(firstMap.get("Symbol"),response.body()?.rates?.get(SharedPreferenceManager().getWidgetFirst(context)) ?: 0.0))
                    }
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }

                override fun onFailure(call: Call<Latest>, t: Throwable) {
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            })

        }

        private fun sendNotification(applicationContext: Context, id: Int, title : String, subtitle : String) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra(NOTIFICATION_ID, id)

            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val titleNotification = title
            val subtitleNotification = subtitle
            val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
            val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_swap_horiz_black_24dp)
                .setContentTitle(titleNotification).setContentText(subtitleNotification)
                .setDefaults(NotificationCompat.DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true)

            notification.priority = NotificationCompat.PRIORITY_MAX

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification.setChannelId(NOTIFICATION_CHANNEL)

                val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

                val channel =
                    NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_HIGH)

                channel.enableLights(true)
                channel.lightColor = Color.GRAY
                channel.enableVibration(true)
                channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                channel.setSound(ringtoneManager, audioAttributes)
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(id, notification.build())
        }

    }


}

