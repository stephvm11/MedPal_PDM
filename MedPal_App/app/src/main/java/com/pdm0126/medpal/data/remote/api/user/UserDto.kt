package com.pdm0126.medpal.data.remote.api.user

import com.pdm0126.medpal.data.local.database.entities.UserEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Long = 0,
    @SerialName("auth_user_id")
    val authUserId: String,
    @SerialName("nombre")
    val firstName: String,
    @SerialName("apellido")
    val lastName: String
)
fun UserDto.toEntity() = UserEntity(
    id = id,
    authUserId = authUserId,
    firstName = firstName,
    lastName = lastName
)
