package pl.michalmaslak.forecastmvvm.data.network.provider

import pl.michalmaslak.forecastmvvm.data.db.entity.WeatherLocation

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation):Boolean
    suspend fun getPreferredLocationToString(): String
}