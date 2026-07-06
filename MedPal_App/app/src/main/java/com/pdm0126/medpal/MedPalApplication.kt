package com.pdm0126.medpal

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.pdm0126.medpal.data.AppProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedPalApplication : Application(){

    val appProvider by lazy { AppProvider(this) }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        CoroutineScope(Dispatchers.IO).launch {
            appProvider.loadSavedSession()
        }

    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alertas de MedPal"
            val descriptionText = "Canal para recordatorios de medicamentos, citas y exámenes"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("MEDPAL_ALERTS_CHANNEL", name, importance).apply {
                description = descriptionText
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}