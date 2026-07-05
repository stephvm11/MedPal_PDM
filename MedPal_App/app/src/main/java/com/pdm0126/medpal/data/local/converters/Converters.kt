package com.pdm0126.medpal.data.local.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class Converters {
    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String?{
        return value?.toString()
    }

    @TypeConverter
    fun fromLocalTime(value: LocalTime?): String?{
        return value?.toString()
    }

    @TypeConverter
    fun toLocalDate(value:String?): LocalDate?{
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun toLocalTime(value: String?): LocalTime?{
        return value?.let { LocalTime.parse(it) }
    }
}