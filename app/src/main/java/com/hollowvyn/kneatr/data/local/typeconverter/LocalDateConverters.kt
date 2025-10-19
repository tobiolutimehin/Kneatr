package com.hollowvyn.kneatr.data.local.typeconverter

import androidx.room.TypeConverter
import com.hollowvyn.kneatr.data.util.DateTimeUtils
import kotlinx.datetime.LocalDate
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object LocalDateConverters {
    @TypeConverter
    fun toLocalDate(epochMillis: Long?): LocalDate? = epochMillis?.let { DateTimeUtils.toLocalDate(it) }

    @OptIn(ExperimentalTime::class)
    @TypeConverter
    fun localDateToEpochDays(date: LocalDate?): Long? = date?.let { DateTimeUtils.toEpochMillis(it) }
}
