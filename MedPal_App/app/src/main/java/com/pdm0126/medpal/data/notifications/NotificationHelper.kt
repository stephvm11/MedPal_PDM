package com.pdm0126.medpal.data.notifications

import android.content.Context
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import com.pdm0126.medpal.R

object NotificationHelper {

    fun showStandardNotification(context: Context, id: Int, title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, "MEDPAL_ALERTS_CHANNEL")
            .setSmallIcon(R.drawable.logo_noti)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        notificationManager.notify(id, builder.build())
    }
}