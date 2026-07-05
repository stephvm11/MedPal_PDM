package com.pdm0126.medpal.data.model

import com.pdm0126.medpal.data.local.database.entities.MedicationReminderEntity

data class MedicationReminder(
    val id: Long = 0,
    val time: String,
    val frequencyDays: Int,
    val lastDose: String? = null,
    val medicationId: Long
)

fun MedicationReminder.toEntity(): MedicationReminderEntity {
    return MedicationReminderEntity(
        id = id,
        time = time,
        lastDose = lastDose,
        frequencyDays = frequencyDays,
        medicationId = medicationId
    )
}