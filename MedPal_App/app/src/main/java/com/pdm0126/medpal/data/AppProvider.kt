package com.pdm0126.medpal.data

import android.content.Context
import com.pdm0126.medpal.data.remote.api.KtorClient
import com.pdm0126.medpal.data.repositories.repositoryAuth.AuthRepository
import com.pdm0126.medpal.data.repositories.repositoryAuth.AuthRepositoryImpl
import com.pdm0126.medpal.data.session.SessionManager
import kotlinx.coroutines.flow.first

class AppProvider(context: Context){

    private val sessionManager = SessionManager(context)

    suspend fun loadSavedSession(){
        val savedToken = sessionManager.accessToken.first()
        if(savedToken != null ){
            KtorClient.accessToken = savedToken
        }
    }

    private val authRepository: AuthRepository = AuthRepositoryImpl(sessionManager)

    fun provideAuthRepository(): AuthRepository {
        return authRepository
    }

}