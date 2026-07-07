package com.pdm0126.medpal.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pdm0126.medpal.data.local.database.entities.AppointmentReminderEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalTime

@Dao
interface AppointmentReminderDao {
    @Query("SELECT * FROM recordatorio_cita WHERE id_cita = :appointmentId")
    fun getRemindersOfAppointment(appointmentId: Long): Flow<List<AppointmentReminderEntity>>

    @Query("SELECT * FROM recordatorio_cita WHERE id_examen = :examId")
    fun getRemindersOfExam(examId: Long): Flow<List<AppointmentReminderEntity>>

    @Query("""
        SELECT * FROM recordatorio_cita 
        WHERE id_cita = :id OR id_examen = :id
    """)
    fun getRemindersByRelatedId(id: Long): Flow<List<AppointmentReminderEntity>>

    @Query("SELECT * FROM recordatorio_cita WHERE id = :reminderId LIMIT 1")
    suspend fun getReminderById(reminderId: Long): AppointmentReminderEntity?

    @Query("DELETE FROM recordatorio_cita WHERE id = :id")
    suspend fun deleteReminderById(id: Long)

    @Query("UPDATE recordatorio_cita SET frecuencia = :frequency, hora = :time, dias_inicio = :startDay WHERE id = :reminderId")
    suspend fun updateReminder(frequency: String, time: LocalTime, startDay: Int, reminderId: Long)

    @Upsert
    suspend fun upsertReminder(reminder: AppointmentReminderEntity)

    @Upsert
    suspend fun upsertReminders(reminders: List<AppointmentReminderEntity>)


}