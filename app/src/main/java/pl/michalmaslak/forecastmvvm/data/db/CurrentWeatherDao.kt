package pl.michalmaslak.forecastmvvm.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.michalmaslak.forecastmvvm.data.db.entity.CURRENT_WEATHER_ID
import pl.michalmaslak.forecastmvvm.data.db.entity.CurrentWeatherEntry
import pl.michalmaslak.forecastmvvm.data.unitlocalized.MetricCurrentWeatherEntry

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntry: CurrentWeatherEntry)

    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getWeatherMetric():LiveData<MetricCurrentWeatherEntry>

}