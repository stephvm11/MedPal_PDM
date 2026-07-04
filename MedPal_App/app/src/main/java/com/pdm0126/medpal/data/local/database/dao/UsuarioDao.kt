package com.pdm0126.medpal.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pdm0126.medpal.data.local.database.entities.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuario WHERE id = :id LIMIT 1")
    fun getUsuarioById(id: Long): Flow<UsuarioEntity>

    @Upsert
    suspend fun upsertUsuario(usuario: UsuarioEntity)
}