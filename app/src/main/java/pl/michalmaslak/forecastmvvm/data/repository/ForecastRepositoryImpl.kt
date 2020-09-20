package pl.michalmaslak.forecastmvvm.data.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.michalmaslak.forecastmvvm.data.db.CurrentWeatherDao
import pl.michalmaslak.forecastmvvm.data.db.WeatherLocationDao
import pl.michalmaslak.forecastmvvm.data.db.entity.WeatherLocation
import pl.michalmaslak.forecastmvvm.data.network.WeatherNetworkDataSource
import pl.michalmaslak.forecastmvvm.data.network.provider.LocationProvider
import pl.michalmaslak.forecastmvvm.data.network.response.CurrentWeatherResponse
import pl.michalmaslak.forecastmvvm.data.unitlocalized.MetricCurrentWeatherEntry
import java.time.ZonedDateTime

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepository {

    init{
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }
    override suspend fun getCurrentWeather(): LiveData<out MetricCurrentWeatherEntry> {
        initWeatherData()
        return withContext(Dispatchers.IO) {
            return@withContext currentWeatherDao.getWeatherMetric()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse){
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation>{
        return withContext(Dispatchers.IO){
            return@withContext weatherLocationDao.getLocation()
        }
    }
    @SuppressLint("NewApi")
    private suspend fun initWeatherData(){
        val lastWeatherLocation= weatherLocationDao.getLocationNonLive()

        if(lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)) {
            fetchCurrentWeather()
            return
        }

        if(isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather()
    }
    @SuppressLint("NewApi")
    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean{
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }

    private suspend fun fetchCurrentWeather(){
        weatherNetworkDataSource.fetchCurrentWeather(locationProvider.getPreferredLocationToString())
    }
}