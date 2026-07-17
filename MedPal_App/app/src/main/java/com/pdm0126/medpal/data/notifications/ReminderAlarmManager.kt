package com.pdm0126.medpal.data.notifications

import android.content.Context
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import com.pdm0126.medpal.navigation.Routes
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.number
import java.util.Calendar

object ReminderAlarmManager {

    fun scheduleAppointmentAlarm(
        context: Context,
        appointmentId: Long,
        title: String,
        description: String,
        hour: Int,
        minute: Int,
        appointmentDate: LocalDate,
        daysBefore: Int,
        frequency: String
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val firstAlarmDate = appointmentDate.minus(daysBefore, DateTimeUnit.DAY)

        val requestCode = 200000 + (appointmentId.toInt() * 2)

        val intent = Intent(context, AlertReceiver::class.java).apply {
            putExtra("ALERT_ID", appointmentId)
            putExtra("ALERT_TYPE", "CITA")
            putExtra("ALERT_TITLE", title)
            putExtra("ALERT_SUBTITLE", description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, firstAlarmDate.year)
            set(Calendar.MONTH, firstAlarmDate.monthNumber - 1)
            set(Calendar.DAY_OF_MONTH, firstAlarmDate.day)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (calendar.before(Calendar.getInstance())) {
            val intervalDays = getDaysFromFrequency(frequency)
            calendar.add(Calendar.DATE, intervalDays)
        }

        val intervalMillis = getIntervalMillis(frequency)

        if (intervalMillis > 0L) {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                intervalMillis,
                pendingIntent
            )
        } else {
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

    private fun getDaysFromFrequency(frequency: String): Int {
        return when (frequency.uppercase()) {
            "DIARIO" -> 1
            "CADA_3_DIAS" -> 3
            "SEMANAL" -> 7
            else -> 1
        }
    }

    private fun getIntervalMillis(frequency: String): Long {
        val days = when (frequency) {
            "DIARIO" -> 1L
            "CADA_3_DIAS" -> 3L
            "SEMANAL" -> 7L
            else -> 0L
        }
        return days * AlarmManager.INTERVAL_DAY
    }

    fun scheduleExamAlarm(
        context: Context,
        examId: Long,
        title: String,
        description: String,
        hour: Int,
        minute: Int,
        examDate: LocalDate,
        daysBefore: Int,
        frequency: String
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val requestCode = 300000 + (examId.toInt() * 2 - 1)

        val firstAlarmDate = examDate.minus(daysBefore, DateTimeUnit.DAY)

        val intent = Intent(context, AlertReceiver::class.java).apply {
            putExtra("ALERT_ID", examId)
            putExtra("ALERT_TYPE", "EXAMEN")
            putExtra("ALERT_TITLE", title)
            putExtra("ALERT_SUBTITLE", description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, firstAlarmDate.year)
            set(Calendar.MONTH, firstAlarmDate.monthNumber - 1)
            set(Calendar.DAY_OF_MONTH, firstAlarmDate.day)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (calendar.before(Calendar.getInstance())) {
            val intervalDays = getDaysFromFrequency(frequency)
            calendar.add(Calendar.DATE, intervalDays)
        }

        val intervalMillis = getIntervalMillis(frequency)

        if (intervalMillis > 0L) {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                intervalMillis,
                pendingIntent
            )
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        }
    }

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
            putExtra("ALERT_TITLE", "Hora de tu medicamento!! ")
            putExtra("ALERT_SUBTITLE", "Es momento de tomar: $medicationName || $dosage")
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
}