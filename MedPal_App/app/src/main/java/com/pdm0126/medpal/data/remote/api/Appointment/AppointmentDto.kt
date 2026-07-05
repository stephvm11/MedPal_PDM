package com.pdm0126.medpal.data.remote.api.Appointment

import com.pdm0126.medpal.data.local.database.entities.AppointmentEntity
import com.pdm0126.medpal.data.model.Appointment
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppointmentDto(
    val id: Int,
    @SerialName("id_usuario") val idUsuario: Int,
    val titulo: String,
    val especialista: String,
    val lugar: String,
    val fecha: LocalDate,
    val hora: LocalTime
)

fun AppointmentDto.toModel(): Appointment{
    return Appointment(
        id = id,
        title = titulo,
        specialist = especialista,
        place = lugar,
        date = fecha,
        time = hora,
        userId = idUsuario
    )
}

fun AppointmentDto.toEntity(): AppointmentEntity{
    return AppointmentEntity(
        id = id,
        title = titulo,
        specialist = especialista,
        place = lugar,
        date = fecha,
        time = hora,
        userId = idUsuario
    )
}