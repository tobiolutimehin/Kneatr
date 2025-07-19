package com.hollowvyn.kneatr.data.util

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class DateTimeHelper(
    private val clock: Clock,
    private val timeZone: TimeZone,
) {
    fun calculateDaysAfter(
        date: LocalDate,
        days: Int,
    ): LocalDate = date.plus(days, DateTimeUnit.DAY)

    fun today(): LocalDate = clock.todayIn(timeZone)
}
