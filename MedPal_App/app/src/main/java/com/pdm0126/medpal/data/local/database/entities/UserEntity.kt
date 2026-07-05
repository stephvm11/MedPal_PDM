package com.pdm0126.medpal.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pdm0126.medpal.data.model.User

@Entity(tableName = "usuario")
data class UserEntity(
    @PrimaryKey
    val id: Long = 0,
    val authUserId: String,
    val firstName: String,
    val lastName: String
)

fun UserEntity.toModel(): User {
    return User(
        id = id,
        authUserId = authUserId,
        firstName = firstName,
        lastName = lastName
    )
}