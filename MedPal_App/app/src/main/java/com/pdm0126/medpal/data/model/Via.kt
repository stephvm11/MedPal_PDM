package com.pdm0126.medpal.data.model

import com.pdm0126.medpal.data.local.database.entities.ViaEntity

data class Via(
    val id: Long = 0,
    val via: String
)

fun Via.toEntity(): ViaEntity {
    return ViaEntity(
        id = id,
        via = via
    )
}