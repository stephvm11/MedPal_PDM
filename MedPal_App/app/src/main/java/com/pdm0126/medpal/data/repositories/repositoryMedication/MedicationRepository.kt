package com.pdm0126.medpal.data.repositories.repositoryMedication

import com.pdm0126.medpal.data.local.database.entities.MedicationWithReminders
import com.pdm0126.medpal.data.model.Medication
import com.pdm0126.medpal.data.model.MedicationReminder
import kotlinx.coroutines.flow.Flow

interface MedicationRepository {

    fun getMedicationsWithReminders(userId: Long): Flow<List<MedicationWithReminders>>

    suspend fun refresh(userId: Long): Result<Unit>

    suspend fun createMedication(
        name: String,
        dosage: String,
        notes: String?,
        administrationRouteId: Long,
        userId: Long
    ): Result<Unit>

    suspend fun deleteMedication(id: Long): Result<Unit>

    suspend fun createReminder(
        time: String,
        frequencyDays: Int,
        medicationId: Long
    ): Result<Unit>

    suspend fun deleteReminder(id: Long): Result<Unit>
}