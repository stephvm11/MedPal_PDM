package com.pdm0126.medpal.data.remote.api.AppointmentReminder

import com.pdm0126.medpal.data.local.database.entities.AppointmentReminderEntity
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppointmentReminderDto(
    val id: Long,
    @SerialName("dias_inicio")
    val startDay: Int,
    @SerialName("frecuencia")
    val frequency: String,
    @SerialName("hora")
    val time: LocalTime,
    @SerialName("id_cita")
    val appointmentId: Long?,
    @SerialName("id_examen")
    val examId: Long?
)

fun AppointmentReminderDto.toEntity() : AppointmentReminderEntity{
    return AppointmentReminderEntity(
        id = id,
        startDay = startDay,
        frequency = frequency,
        time = time,
        appointmentId = appointmentId,
        examId = examId
    )
}