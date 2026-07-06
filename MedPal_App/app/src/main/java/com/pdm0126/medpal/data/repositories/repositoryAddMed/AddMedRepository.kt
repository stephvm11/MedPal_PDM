package com.pdm0126.medpal.data.repositories.repositoryAddMed

import com.pdm0126.medpal.data.model.AdministrationRoute
import kotlinx.coroutines.flow.Flow

interface AddMedRepository {
    fun getAllRoutes(): Flow<List<AdministrationRoute>>

    suspend fun createMedication(
        name: String,
        dosage: String,
        notes: String?,
        administrationRouteId: Long,
        userId: Long
    ): Result<Long>

    suspend fun createReminder(
        time: String,
        frequencyDays: Int,
        medicationId: Long
    ): Result<Unit>
}