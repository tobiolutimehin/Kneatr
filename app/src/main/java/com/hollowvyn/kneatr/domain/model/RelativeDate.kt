package com.hollowvyn.kneatr.domain.model

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

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
