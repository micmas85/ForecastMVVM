package pl.michalmaslak.forecastmvvm.data.db.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val CURRENT_WEATHER_ID = 0
@Entity(tableName = "current_weather")
data class CurrentWeatherEntry(
    @SerializedName("feelslike")
    val feelslike: Double,
    @SerializedName("is_day")
    val isDay: String,
    @SerializedName("precip")
    val precip: Double,
    @SerializedName("temperature")
    val temperature: Double,
    @SerializedName("visibility")
    val visibility: Double,
    @SerializedName("weather_descriptions")
    val weatherDescriptions: List<String>,
    @SerializedName("weather_icons")
    val weatherIcons: List<String>,
    @SerializedName("wind_dir")
    val windDir: String,
    @SerializedName("wind_speed")
    val windSpeed: Double)
{
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID
}