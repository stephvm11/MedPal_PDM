package com.pdm0126.medpal.data.model

import com.pdm0126.medpal.data.local.database.entities.MedicationReminderEntity

data class MedicationReminder(
    val id: Long = 0,
    val time: String,
    val frequencyDays: Int,
    val medicationId: Long
)

fun MedicationReminder.toEntity(): MedicationReminderEntity {
    return MedicationReminderEntity(
        id = id,
        time = time,
        frequencyDays = frequencyDays,
        medicationId = medicationId
    )
}