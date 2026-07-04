package com.pdm0126.medpal.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pdm0126.medpal.data.model.Usuario

@Entity(tableName = "usuario")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val authUserId: String,
    val nombre: String,
    val apellido: String
)

fun UsuarioEntity.toModel(): Usuario {
    return Usuario(
        id = id,
        authUserId = authUserId,
        nombre = nombre,
        apellido = apellido
    )
}