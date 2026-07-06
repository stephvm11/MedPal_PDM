package com.pdm0126.medpal.data.repositories.repositoryAuth

import com.pdm0126.medpal.data.local.database.dao.UserDao
import com.pdm0126.medpal.data.remote.api.KtorClient
import com.pdm0126.medpal.data.remote.api.SupabaseClient
import com.pdm0126.medpal.data.remote.api.user.UserDto
import com.pdm0126.medpal.data.remote.api.user.toEntity
import com.pdm0126.medpal.data.session.SessionManager
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import io.github.jan.supabase.auth.providers.builtin.Email
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import com.pdm0126.medpal.data.local.database.entities.UserEntity
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AuthRepositoryImpl(
    private val sessionManager: SessionManager,
    private val userDao: UserDao
) : AuthRepository {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            SupabaseClient.client.auth.sessionStatus.collect { status ->
                if (status is SessionStatus.Authenticated) {
                    val session = status.session

                    KtorClient.accessToken = session.accessToken

                    val currentStoredToken = sessionManager.accessToken.first()

                    if (currentStoredToken != session.accessToken) {

                        val userId = try {
                            val userInfo: List<UserDto> = KtorClient.client.get("rest/v1/usuario") {
                                parameter("auth_user_id", "eq.${session.user?.id}")
                            }.body()

                            userInfo.firstOrNull()?.id?.toString() ?: ""
                        } catch (e: Exception) {

                            "Error: ${e.message}"
                        }

                        sessionManager.saveSession(
                            accessToken = session.accessToken,
                            refreshToken = session.refreshToken,
                            userId = userId
                        )
                    }
                }
            }
        }
    }

    override val isLoggedIn: Flow<Boolean> =
        sessionManager.accessToken.map { it != null }

    override val userId: Flow<String?> =
        sessionManager.userId

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ) {
        try {
            val authResponse = SupabaseClient.client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            val userEmail = authResponse?.email

            if (userEmail == null) {
                throw Exception("Error en el registro")
            }

            val session = SupabaseClient.client.auth.currentSessionOrNull()

            val userId = session?.user?.id ?: authResponse.userMetadata?.get("id")?.toString()

            if (userId == null) {
                throw Exception("No se pudo obtener el id del usuario")
            }

            if (session != null) {
                sessionManager.saveSession(
                    accessToken = session.accessToken,
                    refreshToken = session.refreshToken,
                    userId = userId
                )
                KtorClient.accessToken = session.accessToken
            }


            Result.success(Unit)

        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ) {
        try {

            SupabaseClient.client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            val session = SupabaseClient.client.auth.currentSessionOrNull()
                ?: throw Exception("No se obtuvo la sesion")

            val user = session.user
                ?: throw Exception("No se obtuvo el usuario")

            KtorClient.accessToken = session.accessToken

            val userInfo: List<UserDto> = KtorClient.client.get("rest/v1/usuario") {
                parameter("auth_user_id", "eq.${user.id}")
            }.body()

            if (userInfo.isEmpty()) {
                throw Exception("El perfil del usuario no está registrado en la base de datos pública")
            }

            val publicUser = userInfo.first()

            userDao.upsertUser(publicUser.toEntity())
            val savedUser = userDao.getUserById(publicUser.id).first()

            sessionManager.saveSession(
                accessToken = session.accessToken,
                refreshToken = session.refreshToken,
                userId = publicUser.id.toString()
            )

        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun logout() {
        SupabaseClient.client.auth.signOut()

        sessionManager.clearSession()

        KtorClient.accessToken = null
    }

    override fun getCurrentUser(id: Long): Flow<UserEntity?> {
        return userDao.getUserById(id)
    }
}