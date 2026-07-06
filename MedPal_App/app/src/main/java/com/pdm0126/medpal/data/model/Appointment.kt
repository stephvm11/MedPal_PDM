package com.pdm0126.medpal.data.model

import com.pdm0126.medpal.data.local.database.entities.AppointmentEntity
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class Appointment(
    val id: Long = 0,
    val title: String,
    val specialist: String,
    val place: String,
    val date: LocalDate,
    val time: LocalTime,
    val userId: Int
)

fun Appointment.toEntity(): AppointmentEntity{
    return AppointmentEntity(
        id = id,
        title = title,
        specialist = specialist,
        place = place,
        date = date,
        time = time,
        userId = userId
    )
}