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
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

class AuthRepositoryImpl(
    private val sessionManager: SessionManager,
    private val userDao: UserDao
) : AuthRepository {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val savedToken = sessionManager.accessToken.first()
            if (savedToken != null) {
                KtorClient.accessToken = savedToken
            }
            SupabaseClient.client.auth.sessionStatus.collect { status ->
                if (status is SessionStatus.NotAuthenticated) {
                    KtorClient.accessToken = null
                } else if (status is SessionStatus.Authenticated) {
                    KtorClient.accessToken = status.session.accessToken
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

            if (authResponse == null || authResponse.email == null) {
                throw Exception("Error en el registro")
            }

            val supabaseUid = authResponse.id

            val session = SupabaseClient.client.auth.currentSessionOrNull()
            val tokenParaInsertar = session?.accessToken

            val userProfileBody = buildJsonObject {
                put("auth_user_id", JsonPrimitive(supabaseUid))
                put("nombre", JsonPrimitive(firstName))
                put("apellido", JsonPrimitive(lastName))
            }

            KtorClient.client.post("rest/v1/usuario") {
                setBody(userProfileBody)
                contentType(ContentType.Application.Json)
                tokenParaInsertar?.let {
                    header(HttpHeaders.Authorization, "Bearer $it")
                }
            }

            if (session != null) {

                val userInfo: List<UserDto> = KtorClient.client.get("rest/v1/usuario") {
                    parameter("auth_user_id", "eq.$supabaseUid")
                }.body()

                val publicUser = userInfo.firstOrNull()

                if (publicUser != null) {
                    userDao.upsertUser(publicUser.toEntity())

                    sessionManager.saveSession(
                        accessToken = session.accessToken,
                        refreshToken = session.refreshToken,
                        userId = publicUser.id.toString()
                    )
                    KtorClient.accessToken = session.accessToken
                }
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
        try {
            SupabaseClient.client.auth.signOut()
        } catch (e: Exception) {
        } finally {

            try {
                userDao.clearAllTables()
            } catch (e: Exception) {
            }
            sessionManager.clearSession()

            KtorClient.accessToken = null
        }
    }

    override fun getCurrentUser(id: Long): Flow<UserEntity?> {
        return userDao.getUserById(id)
    }
}