package com.pdm0126.medpal.data.session

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionManager (
    private val context: Context
){

    companion object{
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val USER_ID = stringPreferencesKey("user_id")
    }

    val accessToken: Flow<String?> =
        context.dataStore.data.map { it[ACCESS_TOKEN] }

    val resfreshToken: Flow<String?> =
        context.dataStore.data.map { it[REFRESH_TOKEN] }

    val userId: Flow<String?> =
        context.dataStore.data.map { it[USER_ID] }

    suspend fun saveSession(
        accessToken: String,
        refreshToken: String,
        userId: String
    ){
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = accessToken
            prefs[REFRESH_TOKEN] = refreshToken
            prefs[USER_ID] = userId
        }
    }

    suspend fun clearSession(){
        context.dataStore.edit { it.clear() }
    }
}