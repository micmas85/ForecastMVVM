package pl.michalmaslak.forecastmvvm.data.network.provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred
import pl.michalmaslak.forecastmvvm.data.db.entity.WeatherLocation
import pl.michalmaslak.forecastmvvm.internal.LocationPremissionNotGrantedException
import pl.michalmaslak.forecastmvvm.internal.asDeferred

const val USE_DEVICE_LOCATION= "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION= "CUSTOM_LOCATION"

class LocationProviderImpl(
        private val fusedLocationProviderClient: FusedLocationProviderClient,
        context: Context
) : PreferenceProvider(context), LocationProvider {

private val appContext = context.applicationContext

    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        val deviceLocationChanged = try {
            hasDeviceLocationChanged(lastWeatherLocation)
        } catch (e:LocationPremissionNotGrantedException){
            false
        }

        return deviceLocationChanged || hasCustomLocationChanged(lastWeatherLocation)
    }

    private fun hasCustomLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        if(!isUsingDeviceLocation()){
            val customLocationName = getCustomLocationName()
            return customLocationName != lastWeatherLocation.name
        }
        return false
    }

    private fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, null)
    }

    private suspend fun hasDeviceLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        if(!isUsingDeviceLocation())
            return false
        val deviceLocation = getLastDeviceLocationAsync().await()
            ?: return false

        val comparsionThreshold = 0.03
        return Math.abs(deviceLocation.latitude - lastWeatherLocation.lat.toDouble()) > comparsionThreshold &&
                Math.abs(deviceLocation.longitude - lastWeatherLocation.lon.toDouble()) > comparsionThreshold
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocationAsync(): Deferred<Location?> {

        return if(hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationPremissionNotGrantedException()
    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    override suspend fun getPreferredLocationToString(): String {
        if(isUsingDeviceLocation()){
            try{
                val deviceLocation= getLastDeviceLocationAsync().await()
                        ?: return "${getCustomLocationName()}"
                return "${deviceLocation.latitude},${deviceLocation.longitude}"
            }catch (e: LocationPremissionNotGrantedException){
                return "${getCustomLocationName()}"
            }
        }
        else{
            return "${getCustomLocationName()}"
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}