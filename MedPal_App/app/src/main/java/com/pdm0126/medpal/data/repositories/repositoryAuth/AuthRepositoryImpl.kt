package com.pdm0126.medpal.data.repositories.repositoryAuth

import com.pdm0126.medpal.data.remote.api.KtorClient
import com.pdm0126.medpal.data.remote.api.SupabaseClient
import com.pdm0126.medpal.data.session.SessionManager
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import io.github.jan.supabase.auth.providers.builtin.Email

class AuthRepositoryImpl (
    private val sessionManager: SessionManager
) : AuthRepository {

    override val isLoggedIn: Flow<Boolean> =
        sessionManager.accessToken.map { it != null }

    override val userName: Flow<String?> =
        sessionManager.userId

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ){
        try {
            val authResponse = SupabaseClient.client.auth.signUpWith(Email){
                this.email = email
                this.password = password
            }

            val userEmail = authResponse?.email

            if (userEmail == null){
                throw Exception("Error en el registro")
            }

            val session = SupabaseClient.client.auth.currentSessionOrNull()

            val userId = session?.user?.id ?: authResponse.userMetadata?.get("id")?.toString()

            if (userId == null){
                throw Exception("No se pudo obtner el id del usuari")
            }

            if (session != null ){
                sessionManager.saveSession(
                    accessToken = session.accessToken,
                    refreshToken = session.refreshToken,
                    userId = userId
                )
                KtorClient.accessToken = session.accessToken
            }


            Result.success(Unit)

        }catch (e: Exception){
            throw e
        }
    }

    override suspend fun login(
        email: String,
        password: String){
         try {

            SupabaseClient.client.auth.signInWith(Email){
                this.email = email
                this.password = password
            }

            val session = SupabaseClient.client.auth.currentSessionOrNull()
                ?: throw Exception("No se obtuvo la sesion")

            val user = session.user
                ?: throw Exception("No se obtuvo el usuario")

            sessionManager.saveSession(
                accessToken = session.accessToken,
                refreshToken = session.refreshToken,
                userId = user.id
            )

            KtorClient.accessToken = session.accessToken

        }catch (e: Exception){
             throw e
        }
    }

    override suspend fun logout() {
        SupabaseClient.client.auth.signOut()

        sessionManager.clearSession()

        KtorClient.accessToken = null
    }
}