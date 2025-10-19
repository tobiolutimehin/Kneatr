package com.hollowvyn.kneatr.data.util

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
object DateTimeUtils {
    private val clock: Clock = Clock.System
    private val timeZone: TimeZone = TimeZone.currentSystemDefault()

    val customFormat =
        LocalDate.Format {
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            day()
            chars(", ")
            year()
        }

    fun calculateDaysAfter(
        date: LocalDate,
        days: Int,
    ): LocalDate = date.plus(days, DateTimeUnit.DAY)

    fun today(): LocalDate = clock.todayIn(timeZone)

    fun toLocalDate(millis: Long): LocalDate =
        Instant
            .fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.UTC)
            .date

    fun toEpochMillis(date: LocalDate): Long = date.atStartOfDayIn(timeZone).toEpochMilliseconds()

    fun formatDate(date: LocalDate): String = date.format(customFormat)

    fun formatDate(millis: Long): String = toLocalDate(millis).format(customFormat)
}
