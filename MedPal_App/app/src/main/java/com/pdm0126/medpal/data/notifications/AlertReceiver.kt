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

        val defaultTitle = when (typeString) {
            "CITA" -> "¡Tenés una cita médica!"
            "EXAMEN" -> "¡Recordatorio de examen!"
            else -> "Hora de tu medicamento"
        }

        val defaultSubtitle = when (typeString) {
            "CITA" -> "Revisá los detalles de tu cita de hoy."
            "EXAMEN" -> "Preparate para tu examen programado."
            else -> "Es hora de tu dosis"
        }

        val title = intent.getStringExtra("ALERT_TITLE") ?: defaultTitle
        val subtitle = intent.getStringExtra("ALERT_SUBTITLE") ?: defaultSubtitle

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