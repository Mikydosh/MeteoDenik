package cz.utb.meteodenik.di

import cz.utb.meteodenik.BuildConfig
import cz.utb.meteodenik.data.remote.MeteoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SitModul {
    private const val URL_PREDPOVED = "https://api.open-meteo.com/"

    @Provides
    @Singleton
    fun poskytniJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun poskytniKlienta(): OkHttpClient = OkHttpClient.Builder()
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

    @Provides
    @Singleton
    fun poskytniRetrofit(klient: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl(URL_PREDPOVED)
            .client(klient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    fun poskytniMeteoApi(retrofit: Retrofit): MeteoApi =
        retrofit.create(MeteoApi::class.java)
}