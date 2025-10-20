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

    fun formatPastDate(date: LocalDate): RelativeDate {
        val today = clock.todayIn(timeZone)
        val daysDifference = (today.toEpochDays() - date.toEpochDays()).toInt()

        return when {
            daysDifference < 0 -> RelativeDate.Absolute(date) // A future date was passed, show it absolutely
            daysDifference == 0 -> RelativeDate.Today
            daysDifference == 1 -> RelativeDate.Yesterday
            daysDifference in 2..6 -> RelativeDate.LastWeekday(date.dayOfWeek)
            daysDifference in 7..13 -> RelativeDate.WeeksAgo(1)
            daysDifference in 14..20 -> RelativeDate.WeeksAgo(2)
            daysDifference in 21..27 -> RelativeDate.WeeksAgo(3)
            else -> RelativeDate.Absolute(date)
        }
    }

    /**
     * Formats a date in the future relative to today.
     */
    fun formatFutureDate(date: LocalDate): RelativeDate {
        val today = clock.todayIn(timeZone)
        // Note: Flipped the calculation for positive numbers and easier logic
        val daysDifference = (date.toEpochDays() - today.toEpochDays()).toInt()

        return when {
            daysDifference < 0 -> RelativeDate.Overdue // A past date was passed, it's overdue
            daysDifference == 0 -> RelativeDate.Today
            daysDifference == 1 -> RelativeDate.Tomorrow
            daysDifference in 2..6 -> RelativeDate.NextWeekday(date.dayOfWeek)
            daysDifference in 7..13 -> RelativeDate.Weeks(1)
            daysDifference in 14..20 -> RelativeDate.Weeks(2)
            daysDifference in 21..27 -> RelativeDate.Weeks(3)
            else -> RelativeDate.Absolute(date)
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
