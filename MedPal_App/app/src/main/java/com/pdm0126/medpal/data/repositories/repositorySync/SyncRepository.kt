package com.pdm0126.medpal.data.repositories.repositorySync

import kotlinx.coroutines.flow.Flow

interface SyncRepository {

    val isSyncing: Flow<Boolean>

    suspend fun syncAllData(userId: Long): Result<Unit>

}