package com.hollowvyn.kneatr.data.local.typeconverter

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.time.LocalDate as JavaLocalDate

object LocalDateConverters {
    @TypeConverter
    fun fromEpochDays(epochDays: Int?): LocalDate? =
        epochDays?.let {
            JavaLocalDate.ofEpochDay(it.toLong()).toKotlinLocalDate()
        }

    @TypeConverter
    fun localDateToEpochDays(date: LocalDate?): Int? =
        date
            ?.toJavaLocalDate()
            ?.toEpochDay()
            ?.toInt()
}
