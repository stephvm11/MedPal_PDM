package com.pdm0126.medpal.data.repositories.repositoryAuth

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isLoggedIn: Flow<Boolean>

    val userName: Flow<String?>
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
}