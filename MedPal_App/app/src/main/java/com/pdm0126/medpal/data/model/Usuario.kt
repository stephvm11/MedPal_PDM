package com.pdm0126.medpal.data.model

import com.pdm0126.medpal.data.local.database.entities.UsuarioEntity

data class Usuario(
    val id: Long = 0,
    val authUserId: String,
    val nombre: String,
    val apellido: String
)

fun Usuario.toEntity(): UsuarioEntity{
    return UsuarioEntity(
        id = id,
        authUserId = authUserId,
        nombre = nombre,
        apellido = apellido
    )
}