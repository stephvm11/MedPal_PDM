package com.pdm0126.medpal.data.remote.api.Medication

import com.pdm0126.medpal.data.local.database.entities.MedicationEntity
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class MedicationDto(
    val id: Long,
    @SerialName("nombre")
    val name: String,
    @SerialName("dosis")
    val dosage: String,
    @SerialName("nota")
    val notes: String? = null,
    @SerialName("ultima_dosis")
    val lastDose: String? = null,
    @SerialName("id_via")
    val administrationRouteId: Long,
    @SerialName("id_usuario")
    val userId: Long
)

fun MedicationDto.toEntity() = MedicationEntity(
    id = id,
    name = name,
    dosage = dosage,
    notes = notes,
    lastDose = lastDose,
    administrationRouteId = administrationRouteId,
    userId = userId
)