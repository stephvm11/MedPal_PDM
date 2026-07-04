package com.pdm0126.medpal.data.remote.api

import android.util.Log
import com.pdm0126.medpal.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClient {

    private const val SUPABASE_URL = BuildConfig.SUPABASE_URL

    var accessToken: String? = null

    val client = HttpClient(OkHttp) {

        expectSuccess = true

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                }
            )
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("KtorClient", message)
                }
            }

            level = LogLevel.ALL
        }

        defaultRequest{
            url(SUPABASE_URL)

            header("apikey", BuildConfig.SUPABASE_ANON_KEY)

            accessToken?.let { header(HttpHeaders.Authorization, "Bearer $it")}

            header(HttpHeaders.Accept, "application/json")
        }

    }
}