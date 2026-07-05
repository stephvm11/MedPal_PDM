package com.pdm0126.medpal.data.model

import com.pdm0126.medpal.data.local.database.entities.MedicationEntity

data class Medication(
    val id: Long = 0,
    val name: String,
    val dosage: String,
    val notes: String? = null,
    val administrationRouteId: Long,
    val userId: Long
)

fun Medication.toEntity(): MedicationEntity {
    return MedicationEntity(
        id = id,
        name = name,
        dosage = dosage,
        notes = notes,
        administrationRouteId = administrationRouteId,
        userId = userId
    )
}