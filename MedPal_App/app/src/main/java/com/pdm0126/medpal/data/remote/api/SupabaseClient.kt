package com.pdm0126.medpal.data.remote.api



import android.util.Log
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import com.pdm0126.medpal.BuildConfig
import io.github.jan.supabase.annotations.SupabaseInternal
import io.ktor.client.plugins.logging.*

object SupabaseClient {

    private const val SUPABASE_URL = BuildConfig.SUPABASE_URL

    private const val SUPABASE_ANON_KEY = BuildConfig.SUPABASE_ANON_KEY

    @OptIn(SupabaseInternal::class)
    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_ANON_KEY
    ) {

        install(Auth)

        httpConfig {
            install(Logging){
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("SupabaseHTTP", message)
                    }
                }
                level = LogLevel.ALL
            }
        }
    }
}