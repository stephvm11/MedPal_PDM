package com.pdm0126.medpal.data.notifications

import android.content.Context
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import java.util.Calendar

object ReminderAlarmManager {

    fun scheduleMedicationAlarm(
        context: Context,
        reminderId: Long,
        medicationName: String,
        dosage: String,
        hour: Int,
        minute: Int,
        daysInterval: Int,
        startDate: LocalDate
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlertReceiver::class.java).apply {
            putExtra("ALERT_ID", reminderId)
            putExtra("ALERT_TYPE", "MEDICAMENTO")
            putExtra("ALERT_TITLE", "Hora de tu medicamento ")
            putExtra("ALERT_SUBTITLE", "Es momento de tomar $medicationName : $dosage")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminderId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {

            set(Calendar.YEAR, startDate.year)
            set(Calendar.MONTH, startDate.month.number - 1)
            set(Calendar.DAY_OF_MONTH, startDate.day)

            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, daysInterval)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {

            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
}