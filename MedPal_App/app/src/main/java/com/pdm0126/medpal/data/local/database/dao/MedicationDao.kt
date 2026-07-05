package com.pdm0126.medpal.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.pdm0126.medpal.data.local.database.entities.MedicationEntity
import com.pdm0126.medpal.data.local.database.entities.MedicationWithReminders
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {

    @Transaction
    @Query("SELECT * FROM medicamento WHERE id_usuario = :userId")
    fun getMedicationsWithRemindersByUser(userId: Long): Flow<List<MedicationWithReminders>>
    @Query("SELECT * FROM medicamento WHERE id_usuario = :userId")
    fun getMedicationsByUser(userId: Long): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medicamento WHERE id = :id LIMIT 1")
    suspend fun getMedicationById(id: Long): MedicationEntity?

    @Upsert
    suspend fun upsertMedication(medication: MedicationEntity)

    @Upsert
    suspend fun upsertMedications(medications: List<MedicationEntity>)

    @Query("DELETE FROM medicamento WHERE id = :id")
    suspend fun deleteMedicationById(id: Long)

}