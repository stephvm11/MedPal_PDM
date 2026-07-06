package com.pdm0126.medpal.data.model

import com.pdm0126.medpal.data.local.database.entities.AppointmentReminderEntity
import kotlinx.datetime.LocalTime

data class AppointmentReminder(
    val id: Long = 0,
    val appointmentId: Long? = null,
    val examId: Long? = null,
    val startDay: Int,
    val time: LocalTime,
    val frequency: String
)

fun AppointmentReminder.toEntity(): AppointmentReminderEntity{
    return AppointmentReminderEntity(
        id = id,
        appointmentId = appointmentId,
        examId = examId,
        startDay = startDay,
        time = time,
        frequency = frequency
    )
}