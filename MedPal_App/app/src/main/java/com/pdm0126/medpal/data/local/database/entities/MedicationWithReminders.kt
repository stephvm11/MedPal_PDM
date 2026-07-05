package com.pdm0126.medpal.data.local.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class MedicationWithReminders(
    @Embedded val medication: MedicationEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id_medicamento"
    )
    val reminders: List<MedicationReminderEntity>
)