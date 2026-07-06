package com.pdm0126.medpal.data.local.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class AppointmentWithReminders(
    @Embedded val appointment: AppointmentEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id_cita"
    )
    val reminders: List<AppointmentReminderEntity>
)