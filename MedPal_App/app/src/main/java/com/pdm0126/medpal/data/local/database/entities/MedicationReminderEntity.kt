package com.pdm0126.medpal.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pdm0126.medpal.data.model.MedicationReminder

@Entity(
    tableName = "recordatorio_medicamento",
    foreignKeys = [
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_medicamento"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("id_medicamento")]
)

data class MedicationReminderEntity(
    @PrimaryKey
    val id: Long = 0,
    @ColumnInfo(name = "hora")
    val time: String,
    @ColumnInfo(name = "frecuencia_dias")
    val frequencyDays: Int,
    @ColumnInfo(name = "ultima_dosis")
    val lastDose: String? = null,
    @ColumnInfo(name = "id_medicamento")
    val medicationId: Long
)

fun MedicationReminderEntity.toModel(): MedicationReminder{
    return MedicationReminder(
        id = id,
        time = time,
        frequencyDays = frequencyDays,
        medicationId = medicationId
    )
}