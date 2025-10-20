package com.hollowvyn.kneatr.domain.util

import androidx.annotation.VisibleForTesting
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

/**
 * A utility object for handling date and time operations using kotlinx.datetime.
 * Provides functions for formatting dates, converting between types, and calculating relative time.
 * This object is designed to be testable by allowing its internal Clock and TimeZone to be replaced during tests.
 */
@OptIn(ExperimentalTime::class)
object DateTimeUtils {
    /** System clock instance */
    @VisibleForTesting
    internal var clock: Clock = Clock.System

    /** System time zone instance */
    @VisibleForTesting
    internal var timeZone: TimeZone = TimeZone.Companion.currentSystemDefault()

    /** current time in milliseconds. */
    @VisibleForTesting
    internal var currentSystemTime = System.currentTimeMillis()

    /**
     * Custom date format for formatting dates.
     */
    private val customFormat =
        LocalDate.Companion.Format {
            monthName(MonthNames.Companion.ENGLISH_ABBREVIATED)
            char(' ')
            day(padding = Padding.ZERO)
            char(',')
            char(' ')
            year()
        }

    /**
     * Formats a date that is in the past into a relative time representation.
     *
     * @param date The local date to format.
     * @return A [RelativeDate] object representing the relationship of the given date to today (e.g., Today, Yesterday, WeeksAgo).
     * If the provided date is in the future, it returns an [RelativeDate.Absolute] representation.
     */
    fun formatPastDate(date: LocalDate): RelativeDate {
        val daysDifference = (today().toEpochDays() - date.toEpochDays()).toInt()

        return when {
            daysDifference < 0 -> RelativeDate.Absolute(date)
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
     * Formats a date that is in the future into a relative time representation.
     *
     * @param date The local date to format.
     * @return A [RelativeDate] object representing the relationship of the given date to today (e.g., Today, Tomorrow, Weeks).
     * If the provided date is in the past, it returns [RelativeDate.Overdue].
     */
    fun formatFutureDate(date: LocalDate): RelativeDate {
        val daysDifference = (date.toEpochDays() - today().toEpochDays()).toInt()

        return when {
            daysDifference < 0 -> RelativeDate.Overdue
            daysDifference == 0 -> RelativeDate.Today
            daysDifference == 1 -> RelativeDate.Tomorrow
            daysDifference in 2..6 -> RelativeDate.NextWeekday(date.dayOfWeek)
            daysDifference in 7..13 -> RelativeDate.Weeks(1)
            daysDifference in 14..20 -> RelativeDate.Weeks(2)
            daysDifference in 21..27 -> RelativeDate.Weeks(3)
            else -> RelativeDate.Absolute(date)
        }
    }

    /**
     * Formats a [LocalDate] into a string (e.g., "Jan 01, 2024").
     *
     * @param date The local date to format.
     * @return A formatted string representation of the date.
     */
    fun formatDate(date: LocalDate): String = date.format(customFormat)

    /**
     * Formats a timestamp in milliseconds into a string (e.g., "Jan 01, 2024").
     *
     * @param millis The timestamp in milliseconds since the epoch.
     * @return A formatted string representation of the date.
     */
    fun formatDate(millis: Long): String = toLocalDate(millis).format(customFormat)

    /**
     * Converts a timestamp in milliseconds to a [LocalDate].
     *
     * @param millis The timestamp in milliseconds since the epoch.
     * @return The corresponding [LocalDate].
     */
    fun toLocalDate(millis: Long): LocalDate =
        Instant.Companion
            .fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.Companion.UTC)
            .date

    /**
     * Returns a [SelectableDates] object for use with a Compose `DatePicker`.
     * This configuration disables selection of future dates and years, allowing only the past and present.
     *
     * @return A [SelectableDates] instance that can be used by a `DatePicker`.
     */
    fun getSelectablePastAndPresentDates() =
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis <= currentSystemTime

            override fun isSelectableYear(year: Int): Boolean = year <= today().year
        }

    /**
     * Calculates a new date by adding a specified number of days to a given date.
     *
     * @param date The starting local date.
     * @param days The number of days to add.
     * @return A new [LocalDate] that is [days] after the original [date].
     */
    fun calculateDaysAfter(
        date: LocalDate,
        days: Int,
    ): LocalDate = date.plus(days, DateTimeUnit.Companion.DAY)

    /**
     * Gets the current date based on the configured clock and time zone.
     *
     * @return The current [LocalDate].
     */
    fun today(): LocalDate = clock.todayIn(timeZone)

    /**
     * Converts a [LocalDate] to its corresponding epoch milliseconds representation.
     *
     * @param date The local date to convert.
     * @return The number of milliseconds since the epoch for the start of the given date in the configured time zone.
     */
    fun toEpochMillis(date: LocalDate): Long = date.atStartOfDayIn(timeZone).toEpochMilliseconds()
}
