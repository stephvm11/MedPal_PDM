package com.pdm0126.medpal.data.repositories.repositoryAuth

import com.pdm0126.medpal.data.local.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isLoggedIn: Flow<Boolean>

    val userId: Flow<String?>
    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String)

    suspend fun login(
        email: String,
        password: String,
    )

    suspend fun logout()

    fun getCurrentUser(id: Long): Flow<UserEntity?>
}