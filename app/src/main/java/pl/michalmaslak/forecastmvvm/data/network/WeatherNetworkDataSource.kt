package pl.michalmaslak.forecastmvvm.data.network

import androidx.lifecycle.LiveData
import pl.michalmaslak.forecastmvvm.data.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String
    )

}