package com.pdm0126.medpal.data.remote.api.Appointment

import com.pdm0126.medpal.data.local.database.entities.AppointmentEntity
import com.pdm0126.medpal.data.model.Appointment
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppointmentDto(
    val id: Long,
    @SerialName("id_usuario")
    val userId: Long,
    @SerialName("titulo")
    val title: String,
    @SerialName("especialista")
    val specialist: String,
    @SerialName("lugar")
    val place: String,
    @SerialName("fecha")
    val date: LocalDate,
    @SerialName("hora")
    val time: LocalTime,
    @SerialName("estado_finalizacion")
    val status: Boolean = false
)

fun AppointmentDto.toModel(): Appointment{
    return Appointment(
        id = id,
        title = title,
        specialist = specialist,
        place = place,
        date = date,
        time = time,
        status = status,
        userId = userId
    )
}

fun AppointmentDto.toEntity(): AppointmentEntity{
    return AppointmentEntity(
        id = id,
        title = title,
        specialist = specialist,
        place = place,
        date = date,
        time = time,
        status = status,
        userId = userId
    )
}