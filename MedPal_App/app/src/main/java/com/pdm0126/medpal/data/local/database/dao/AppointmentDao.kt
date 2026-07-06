package com.pdm0126.medpal.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.pdm0126.medpal.data.local.database.entities.AppointmentEntity
import com.pdm0126.medpal.data.local.database.entities.AppointmentWithReminders
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Dao
interface AppointmentDao {

    @Transaction
    @Query("SELECT * FROM cita WHERE id_usuario = :userId ORDER BY fecha ASC, hora ASC")
    fun getAppointmentsWithRemindersByUser(userId: Long): Flow<List<AppointmentWithReminders>>

    @Query("SELECT * FROM cita WHERE id_usuario = :userId AND estado_finalizacion = 0 ORDER BY fecha ASC, hora ASC")
    fun getPendingAppointmentsWithReminderByUser(userId: Long): Flow<List<AppointmentWithReminders>>

    @Query("SELECT * FROM cita WHERE id_usuario = :userId AND estado_finalizacion = 1 ORDER BY fecha ASC, hora ASC")
    fun getCompletedAppointmentsWithReminderByUser(userId: Long): Flow<List<AppointmentWithReminders>>

    @Query("SELECT * FROM cita WHERE id_usuario = :userId AND fecha = :date AND estado_finalizacion = 0 ORDER BY hora ASC")
    fun getTodayAppointments(userId: Long, date: LocalDate): Flow<List<AppointmentEntity>>

    @Query(
        """
        SELECT * FROM cita 
        WHERE id_usuario = :userId 
        AND fecha BETWEEN :startDate AND :endDate 
        AND estado_finalizacion = 0
        ORDER BY fecha ASC, hora ASC
    """
    )
    fun getWeeklyAppointments(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<AppointmentEntity>>

    @Query("UPDATE cita SET estado_finalizacion = 1 WHERE id = :appointmentId")
    suspend fun completeAppointment(appointmentId: Long)

    @Query("UPDATE cita SET estado_finalizacion = 0 WHERE id = :appointmentId")
    suspend fun uncompletedAppointment(appointmentId: Long)

    @Upsert
    suspend fun upsertAppointment(appointment: AppointmentEntity)

    @Upsert
    suspend fun upsertAppointments(appointment: List<AppointmentEntity>)

}
