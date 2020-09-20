package pl.michalmaslak.forecastmvvm.utils

import androidx.room.TypeConverter


class Converters {
        @TypeConverter
        fun fromString(value: String): List<String> {
            return value.split(",").map { it }
        }
        @TypeConverter
        fun fromList(list: List<String>): String {
            return list.joinToString(separator = ",")
        }
    }