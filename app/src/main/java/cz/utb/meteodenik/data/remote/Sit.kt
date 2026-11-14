package cz.utb.meteodenik.data.remote

import cz.utb.meteodenik.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

/** Dočasné místo, kde se síťová vrstva sestavuje. V lekci 12 ho nahradí Hilt. */
object Sit {
    private const val URL_PREDPOVED = "https://api.open-meteo.com/"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val klient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
            }
        }
        .build()

    val meteoApi: MeteoApi by lazy {
        Retrofit.Builder()
            .baseUrl(URL_PREDPOVED)
            .client(klient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(MeteoApi::class.java)
    }
}