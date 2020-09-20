package pl.michalmaslak.forecastmvvm.data.unitlocalized

import androidx.room.ColumnInfo

data class MetricCurrentWeatherEntry(
    @ColumnInfo(name = "temperature")
    val temperature: Double,
    @ColumnInfo(name = "weatherDescriptions")
    val weatherDescriptions: String,
    @ColumnInfo(name = "weatherIcons")
    val weatherIcons: String,
    @ColumnInfo(name = "precip")
    val precipitationVolume: Double,
    @ColumnInfo(name = "feelslike")
    val feelsLikeTemperature: Double,
    @ColumnInfo(name = "visibility")
    val visibilityDistance: Double,
    @ColumnInfo(name = "windSpeed")
    val windSpeed: Double,
    @ColumnInfo(name = "windDir")
    val windDirection: String
)