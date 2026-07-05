package com.pdm0126.medpal.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pdm0126.medpal.data.local.database.entities.AdministrationRouteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AdministrationRouteDao {

    @Query("SELECT * FROM via")
    fun getAllAdministrationRoutes(): Flow<List<AdministrationRouteEntity>>

    @Upsert
    suspend fun upsertAdministrationRoutes(routes: List<AdministrationRouteEntity>)
}