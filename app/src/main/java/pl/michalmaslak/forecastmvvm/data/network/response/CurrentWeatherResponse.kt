package pl.michalmaslak.forecastmvvm.data.network.response


import com.google.gson.annotations.SerializedName
import pl.michalmaslak.forecastmvvm.data.db.entity.CurrentWeatherEntry
import pl.michalmaslak.forecastmvvm.data.db.entity.WeatherLocation

data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    @SerializedName("location")
    val location: WeatherLocation
)