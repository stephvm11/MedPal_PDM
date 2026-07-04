package com.pdm0126.medpal.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pdm0126.medpal.data.local.database.entities.ViaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ViaDao{
    @Query("SELECT * FROM via")
    fun getAllVias(): Flow<List<ViaEntity>>

    @Upsert
    suspend fun upsertVias(vias: List<ViaEntity>)

}