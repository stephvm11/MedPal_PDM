package com.pdm0126.medpal.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pdm0126.medpal.data.model.Appointment
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime


@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val specialist: String,
    val place: String,
    val date: LocalDate,
    val time: LocalTime,
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