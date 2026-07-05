package com.pdm0126.medpal.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pdm0126.medpal.data.local.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM usuario WHERE id = :id LIMIT 1")
    fun getUserById(id: Long): Flow<UserEntity?>

    @Upsert
    suspend fun upsertUser(user: UserEntity)
}