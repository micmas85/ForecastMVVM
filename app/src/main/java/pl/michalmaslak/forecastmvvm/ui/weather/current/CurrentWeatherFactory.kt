package pl.michalmaslak.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.michalmaslak.forecastmvvm.data.repository.ForecastRepository

class CurrentWeatherViewModelFactory (
    private val forecastRepository: ForecastRepository
) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(forecastRepository) as T
    }

}