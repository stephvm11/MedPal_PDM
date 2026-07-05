package com.pdm0126.medpal.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pdm0126.medpal.data.model.AdministrationRoute

@Entity(tableName = "via")
data class AdministrationRouteEntity(
    @PrimaryKey
    val id: Long = 0,
    @ColumnInfo(name = "via")
    val route: String
)

fun AdministrationRouteEntity.toModel(): AdministrationRoute {
    return AdministrationRoute(
        id = id,
        route = route
    )
}