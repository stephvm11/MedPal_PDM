package com.pdm0126.medpal.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalTime

@Entity(
    tableName = "recordatorio_cita",
    foreignKeys = [
        ForeignKey(
            entity = AppointmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_cita"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [Index("id_cita")]
)

data class AppointmentReminderEntity(
    @PrimaryKey
    val id: Long = 0,
    @ColumnInfo(name = "dias_inicio")
    val startDay: Int,
    @ColumnInfo(name = "frecuencia")
    val frequency: String,
    @ColumnInfo(name = "time")
    val time: LocalTime,
    @ColumnInfo(name = "id_cita")
    val appointmentId: Long,
    @ColumnInfo(name = "id_examen")
    val examId: Long
)

fun AppointmentReminderEntity.toModel()