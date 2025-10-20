package com.hollowvyn.kneatr.domain.util

import androidx.compose.material3.SelectableDates
import com.hollowvyn.kneatr.domain.model.RelativeDate
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
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
    private val timeZone: TimeZone = TimeZone.Companion.currentSystemDefault()

    val customFormat =
        LocalDate.Companion.Format {
            monthName(MonthNames.Companion.ENGLISH_ABBREVIATED)
            char(' ')
            day(padding = Padding.ZERO)
            char(',')
            char(' ')
            year()
        }

    fun formatDateRelatively(date: LocalDate): RelativeDate {
        val today = clock.todayIn(timeZone)
        val daysDifference = (today.toEpochDays() - date.toEpochDays()).toInt()

        return when (daysDifference) {
            in -26..-21 -> {
                RelativeDate.Weeks(3)
            }

            in -20..-14 -> {
                RelativeDate.Weeks(3)
            }

            in -13..-7 -> {
                RelativeDate.Weeks(2)
            }

            in -6..-2 -> {
                RelativeDate.NextWeekday(date.dayOfWeek)
            }

            -1 -> RelativeDate.Tomorrow
            0 -> RelativeDate.Today
            1 -> RelativeDate.Yesterday
            in 2..6 -> {
                RelativeDate.LastWeekday(date.dayOfWeek)
            }

            in 7..13 -> {
                RelativeDate.WeeksAgo(1)
            }

            in 14..20 -> {
                RelativeDate.WeeksAgo(2)
            }

            in 21..26 -> {
                RelativeDate.WeeksAgo(3)
            }

            else -> {
                RelativeDate.Absolute(date)
            }
        }
    }

    fun formatDate(date: LocalDate): String = date.format(customFormat)

    fun formatDate(millis: Long): String = toLocalDate(millis).format(customFormat)

    fun toLocalDate(millis: Long): LocalDate =
        Instant.Companion
            .fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.Companion.UTC)
            .date

    fun getSelectablePastAndPresentDates() =
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis <= System.currentTimeMillis()

            override fun isSelectableYear(year: Int): Boolean = year <= today().year
        }

    fun calculateDaysAfter(
        date: LocalDate,
        days: Int,
    ): LocalDate = date.plus(days, DateTimeUnit.Companion.DAY)

    fun today(): LocalDate = clock.todayIn(timeZone)

    fun toEpochMillis(date: LocalDate): Long = date.atStartOfDayIn(timeZone).toEpochMilliseconds()
}
