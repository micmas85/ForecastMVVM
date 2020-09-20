package pl.michalmaslak.forecastmvvm

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import pl.michalmaslak.forecastmvvm.data.db.ForecastDatabase
import pl.michalmaslak.forecastmvvm.data.network.*
import pl.michalmaslak.forecastmvvm.data.network.provider.LocationProviderImpl
import pl.michalmaslak.forecastmvvm.data.repository.ForecastRepository
import pl.michalmaslak.forecastmvvm.data.repository.ForecastRepositoryImpl
import pl.michalmaslak.forecastmvvm.ui.weather.current.CurrentWeatherViewModelFactory

class ForecastApplication : Application(), KodeinAware{
    override val kodein=  Kodein.lazy {
        import(androidXModule(this@ForecastApplication))

        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().weatherLocationDao() }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>())}

        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { WeatherApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind<pl.michalmaslak.forecastmvvm.data.network.provider.LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(), instance(), instance(),instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance()) }

    }
    override fun onCreate(){
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}