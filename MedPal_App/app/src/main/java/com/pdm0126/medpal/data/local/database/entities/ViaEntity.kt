package com.pdm0126.medpal.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pdm0126.medpal.data.model.Via

@Entity(tableName = "via")
data class ViaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val via: String
)

fun ViaEntity.toModel(): Via {
    return Via(
        id = id,
        via = via
    )
}