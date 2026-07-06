package com.pdm0126.medpal.data.remote.api.MedicationReminder

import com.pdm0126.medpal.data.local.database.entities.MedicationReminderEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MedicationReminderDto(
    val id: Long,
    @SerialName("hora")
    val time: String,
    @SerialName("frecuencia_dias")
    val frequencyDays: Int,
    @SerialName("fecha_inicio")
    val lastDose: String? = null,
    @SerialName("id_medicamento")
    val medicationId: Long
)

fun MedicationReminderDto.toEntity() = MedicationReminderEntity(
    id = id,
    time = time,
    frequencyDays = frequencyDays,
    lastDose = lastDose,
    medicationId = medicationId
)