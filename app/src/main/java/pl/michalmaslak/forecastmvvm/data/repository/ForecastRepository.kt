package pl.michalmaslak.forecastmvvm.data.repository

import androidx.lifecycle.LiveData
import pl.michalmaslak.forecastmvvm.data.db.entity.WeatherLocation
import pl.michalmaslak.forecastmvvm.data.unitlocalized.MetricCurrentWeatherEntry

interface ForecastRepository {
    suspend fun getCurrentWeather(): LiveData<out MetricCurrentWeatherEntry>
    suspend fun getWeatherLocation(): LiveData<WeatherLocation>

}