package com.pdm0126.medpal.data.model

import com.pdm0126.medpal.data.local.database.entities.UserEntity

data class User(
    val id: Long = 0,
    val authUserId: String,
    val firstName: String,
    val lastName: String
)

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        authUserId = authUserId,
        firstName = firstName,
        lastName = lastName
    )
}