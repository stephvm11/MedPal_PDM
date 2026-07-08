package com.pdm0126.medpal.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pdm0126.medpal.data.model.Exam

@Entity(
    tableName = "examen",
    foreignKeys = [
        ForeignKey(
            entity = AppointmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_cita"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("id_cita"),
        Index("estado_finalizacion")
    ]
)

data class ExamEntity(
    @PrimaryKey
    val id: Long = 0,
    @ColumnInfo(name = "titulo")
    val title: String,
    @ColumnInfo(name = "lugar")
    val place: String,
    @ColumnInfo(name = "id_cita")
    val appointmentId: Long,
    @ColumnInfo(name = "estado_finalizacion")
    val status: Boolean = false
)

fun ExamEntity.toModel(): Exam {
    return Exam(
        id = id,
        title = title,
        place = place,
        appointmentId = appointmentId,
        status = status
    )
}