package com.pdm0126.medpal.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.pdm0126.medpal.data.local.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM usuario WHERE id = :id LIMIT 1")
    fun getUserById(id: Long): Flow<UserEntity?>

    @Upsert
    suspend fun upsertUser(user: UserEntity)

    @Query("DELETE FROM usuario")
    suspend fun clearUserData()

    @Query("DELETE FROM cita")
    suspend fun clearCitasData()

    @Query("DELETE FROM examen")
    suspend fun clearExamenesData()

    @Query("DELETE FROM recordatorio_cita")
    suspend fun clearRecordatoriosData()

    @Transaction
    suspend fun clearAllTables() {
        clearRecordatoriosData()
        clearExamenesData()
        clearCitasData()
        clearUserData()
    }
}