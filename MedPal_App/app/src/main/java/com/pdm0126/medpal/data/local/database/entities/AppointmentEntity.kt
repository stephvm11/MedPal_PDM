package com.pdm0126.medpal.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.util.TableInfo
import com.pdm0126.medpal.data.model.Appointment
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Entity(
    tableName = "cita",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("id_usuario")
    ]
)
data class AppointmentEntity(
    @PrimaryKey
    val id: Long = 0,
    @ColumnInfo(name = "titulo")
    val title: String,
    @ColumnInfo(name = "especialista")
    val specialist: String,
    @ColumnInfo(name = "lugar")
    val place: String,
    @ColumnInfo(name = "fecha")
    val date: LocalDate,
    @ColumnInfo(name = "hora")
    val time: LocalTime,
    @ColumnInfo(name = "id_usuario")
    val userId: Int
)

fun AppointmentEntity.toModel(): Appointment {
    return Appointment(
        id = id,
        title = title,
        specialist = specialist,
        place = place,
        date = date,
        time = time,
        userId = userId
    )
}