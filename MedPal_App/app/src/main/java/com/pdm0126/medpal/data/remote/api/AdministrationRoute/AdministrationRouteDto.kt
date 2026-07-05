package com.pdm0126.medpal.data.remote.api.AdministrationRoute

import com.pdm0126.medpal.data.local.database.entities.AdministrationRouteEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdministrationRouteRemoteDto(
    @SerialName("id")
    val id: Long,
    @SerialName("via")
    val via: String
)

fun AdministrationRouteRemoteDto.toEntity(): AdministrationRouteEntity {
    return AdministrationRouteEntity(
        id = this.id,
        route = this.via
    )
}