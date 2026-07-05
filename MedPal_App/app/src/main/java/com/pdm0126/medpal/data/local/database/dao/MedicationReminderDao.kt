package com.pdm0126.medpal.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pdm0126.medpal.data.local.database.entities.MedicationReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationReminderDao {

    @Query("SELECT * FROM recordatorio_medicamento WHERE id_medicamento = :medicationId") // Usa 'medicationId'
    fun getRemindersByMedication(medicationId: Long): Flow<List<MedicationReminderEntity>>

    @Upsert
    suspend fun upsertReminder(reminder: MedicationReminderEntity)

    @Upsert
    suspend fun upsertReminders(reminders: List<MedicationReminderEntity>)

    @Query("DELETE FROM recordatorio_medicamento WHERE id = :id")
    suspend fun deleteReminderById(id: Long)

    @Query("SELECT * FROM recordatorio_medicamento WHERE id = :reminderId LIMIT 1")
    suspend fun getReminderById(reminderId: Long): MedicationReminderEntity?

    @Query("UPDATE recordatorio_medicamento SET ultima_dosis = :timestamp WHERE id = :reminderId")
    suspend fun updateLastDoseByReminderId(reminderId: Long, timestamp: String)
}