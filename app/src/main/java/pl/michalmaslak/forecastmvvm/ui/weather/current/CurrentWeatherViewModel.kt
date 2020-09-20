package pl.michalmaslak.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModel
import pl.michalmaslak.forecastmvvm.data.repository.ForecastRepository
import pl.michalmaslak.forecastmvvm.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather()
    }
    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }

}