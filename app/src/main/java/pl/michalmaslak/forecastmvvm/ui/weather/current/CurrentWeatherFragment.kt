package pl.michalmaslak.forecastmvvm.ui.weather.current

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import pl.michalmaslak.forecastmvvm.R
import pl.michalmaslak.forecastmvvm.internal.glide.GlideApp
import pl.michalmaslak.forecastmvvm.ui.base.ScopedFragment

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentWeatherViewModel::class.java)
        bindUI()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun bindUI()= launch {
            val currentWeather = viewModel.weather.await()
            val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(this@CurrentWeatherFragment, Observer{location ->
            if(location == null ) return@Observer
            updateLocation(location.name)

        })
        currentWeather.observe(this@CurrentWeatherFragment, Observer {
                if(it == null) return@Observer
                    group_loading.visibility = View.GONE
                    updateDateToToday()
                    updateTemperatures(it.temperature, it.feelsLikeTemperature)
                    updatePrecipitation(it.precipitationVolume)
                    updateWind(it.windDirection, it.windSpeed)
                    updateVisibility(it.visibilityDistance)
                    updateCondition(it.weatherDescriptions)
                    GlideApp.with(this@CurrentWeatherFragment)
                        .load("${it.weatherIcons}")
                        .into(imageView_condition_icon)
            })
        }
    private fun updateLocation(location: String){
        (activity as? AppCompatActivity)?.supportActionBar?.title =location
    }
    private fun updateDateToToday(){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperatures(temperature: Double, feelslike: Double){
        textView_temperature.text= "$temperature °C"
        textView_feels_like_temperature.text= "Feels like $feelslike °C"
    }

    private fun updateCondition(condition: String){
        textView_condition.text= condition
    }

    private fun updatePrecipitation(precipitationVolume: Double){
        textView_precipitation.text= "Precipitation: $precipitationVolume mm"
    }

    private fun updateWind(windDirection: String, windSpeed: Double){
        textView_wind.text= "Wind: $windDirection, $windSpeed kph"
    }

    private fun updateVisibility(visibilityDistance: Double){
        textView_visibility.text= "Visibility: $visibilityDistance km"
    }

}

