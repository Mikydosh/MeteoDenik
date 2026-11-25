package cz.utb.meteodenik.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Tvar JSONu z Open-Meteo, jen pole, která čteme. */
@Serializable
data class PredpovedDto(
    val current: AktualneDto
)

@Serializable
data class AktualneDto(
    val time: String,
    @SerialName("temperature_2m") val teplotaC: Double,
    @SerialName("relative_humidity_2m") val vlhkostProcent: Int,
    @SerialName("weather_code") val kodPocasi: Int,
    @SerialName("wind_speed_10m") val rychlostVetruKmH: Double
)