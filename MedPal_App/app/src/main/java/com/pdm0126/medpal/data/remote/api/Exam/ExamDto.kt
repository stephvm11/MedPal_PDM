package com.pdm0126.medpal.data.remote.api.Exam

import com.pdm0126.medpal.data.local.database.entities.ExamEntity
import com.pdm0126.medpal.data.model.Exam
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExamDto(
    val id: Long,
    @SerialName("titulo")
    val title: String,
    @SerialName("lugar")
    val place: String,
    @SerialName("id_cita")
    val appointmentId: Long,
    @SerialName("estado_finalizacion")
    val status: Boolean = false
)

fun ExamDto.toModel(): Exam{
    return Exam(
        id = id,
        title = title,
        place = place,
        appointmentId = appointmentId,
        status = status
    )
}

fun ExamDto.toEntity(): ExamEntity {
    return ExamEntity(
        id = id,
        title = title,
        place = place,
        appointmentId = appointmentId,
        status = status
    )
}