package com.pdm0126.medpal.data.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pdm0126.medpal.data.model.AlertData
import com.pdm0126.medpal.data.model.AlertType
import java.util.Calendar


class AlertReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val id = intent.getLongExtra("ALERT_ID", -1L)
        val typeString = intent.getStringExtra("ALERT_TYPE") ?: "MEDICAMENTO"
        val title = intent.getStringExtra("ALERT_TITLE") ?: "Recordatorio"
        val subtitle = intent.getStringExtra("ALERT_SUBTITLE") ?: "Es hora de tu dosis"

        val alertType = when (typeString) {
            "CITA" -> AlertType.CITA
            "EXAMEN" -> AlertType.EXAMEN
            else -> AlertType.MEDICAMENTO
        }

        val alertData = AlertData(
            id = id,
            type = alertType,
            title = title,
            subtitle = subtitle
        )

        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        val appProcesses = activityManager.runningAppProcesses

        val isAppInForeground = appProcesses?.any {
            it.processName == context.packageName &&
                    it.importance == android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
        } ?: false


        if (isAppInForeground && AlertGlobalEvent.hasActiveSubscribers()) {
            AlertGlobalEvent.triggerAlert(alertData)
        } else {
            NotificationHelper.showStandardNotification(
                context = context,
                id = id.toInt(),
                title = title,
                message = subtitle
            )
        }
    }
}