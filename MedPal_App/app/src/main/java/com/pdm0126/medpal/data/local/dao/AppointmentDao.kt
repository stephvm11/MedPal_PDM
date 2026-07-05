package com.pdm0126.medpal.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.pdm0126.medpal.data.local.entities.AppointmentEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Dao
interface AppointmentDao {

    @Transaction
    @Query("SELECT * FROM appointments ORDER BY date ASC, time ASC")
    fun getAppointments(): Flow<List<AppointmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: AppointmentEntity)

    @Delete
    suspend fun deleteAppointment(appointment: AppointmentEntity)

    @Update
    suspend fun updateAppointment(appointment: AppointmentEntity)

    @Upsert
    suspend fun upsertAppointment(appointment: AppointmentEntity)

    @Upsert
    suspend fun upsertAppointments(appointment: List<AppointmentEntity>)

    @Query("DELETE FROM appointments WHERE id =:id")
    suspend fun deleteAppointmentById(id: Int)


    @Query("UPDATE appointments SET title = :title, specialist = :specialist, place = :place, date = :date, time = :time where id = :id")
    suspend fun updateAppointment(
        title: String,
        specialist: String,
        place: String,
        date: LocalDate,
        time: LocalTime,
        id: Int
    )


}