// In: app/src/main/java/com/hollowvyn/kneatr/data/util/DateTimeUtils.kt

package com.hollowvyn.kneatr.data.util

import androidx.compose.material3.SelectableDates
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
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
    private val timeZone: TimeZone = TimeZone.currentSystemDefault()

    val customFormat =
        LocalDate.Format {
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            day(padding = Padding.ZERO)
            char(',')
            char(' ')
            year()
        }

    fun formatDateRelatively(
        date: LocalDate,
        isFuture: Boolean = false,
    ): RelativeDate {
        val today = clock.todayIn(timeZone)
        val daysDifference = (today.toEpochDays() - date.toEpochDays()).toInt()

        return when {
            isFuture && daysDifference > 0 -> {
                RelativeDate.Overdue
            }

            daysDifference in -26..-21 -> {
                RelativeDate.Weeks(3)
            }

            daysDifference in -20..-14 -> {
                RelativeDate.Weeks(3)
            }

            daysDifference in -13..-7 -> {
                RelativeDate.Weeks(2)
            }

            daysDifference in -6..-2 -> {
                RelativeDate.NextWeekday(date.dayOfWeek)
            }

            daysDifference == -1 -> RelativeDate.Tomorrow
            daysDifference == 0 -> RelativeDate.Today
            daysDifference == 1 -> RelativeDate.Yesterday
            daysDifference in 2..6 -> {
                RelativeDate.LastWeekday(date.dayOfWeek)
            }

            daysDifference in 7..13 -> {
                RelativeDate.WeeksAgo(1)
            }

            daysDifference in 14..20 -> {
                RelativeDate.WeeksAgo(2)
            }

            daysDifference in 21..26 -> {
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
        Instant
            .fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.UTC)
            .date

    fun getSelectablePastAndPresentDates() =
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis <= System.currentTimeMillis()

            override fun isSelectableYear(year: Int): Boolean = year <= today().year
        }

    fun calculateDaysAfter(
        date: LocalDate,
        days: Int,
    ): LocalDate = date.plus(days, DateTimeUnit.DAY)

    fun today(): LocalDate = clock.todayIn(timeZone)

    fun toEpochMillis(date: LocalDate): Long = date.atStartOfDayIn(timeZone).toEpochMilliseconds()
}

sealed interface RelativeDate {
    data object Today : RelativeDate

    data object Yesterday : RelativeDate

    data object Tomorrow : RelativeDate

    data class Weeks(
        val count: Int,
    ) : RelativeDate

    data class LastWeekday(
        val dayOfWeek: DayOfWeek,
    ) : RelativeDate

    data class NextWeekday(
        val dayOfWeek: DayOfWeek,
    ) : RelativeDate

    data class WeeksAgo(
        val count: Int,
    ) : RelativeDate

    data class Absolute(
        val date: LocalDate,
    ) : RelativeDate

    data object Overdue : RelativeDate
}
