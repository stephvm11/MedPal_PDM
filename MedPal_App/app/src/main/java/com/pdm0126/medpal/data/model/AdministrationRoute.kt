package com.pdm0126.medpal.data.model

import com.pdm0126.medpal.data.local.database.entities.AdministrationRouteEntity

data class AdministrationRoute(
    val id: Long = 0,
    val route: String
)

fun AdministrationRoute.toEntity(): AdministrationRouteEntity {
    return AdministrationRouteEntity(
        id = id,
        route = route
    )
}