package com.pdm0126.medpal.data.local.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ExamWithReminders(
    @Embedded val exam: ExamEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id_examen"
    )
    val reminders: List<AppointmentReminderEntity>
)