package com.hollowvyn.kneatr.data.local.util

import com.hollowvyn.kneatr.data.util.DateTimeHelper
import com.hollowvyn.kneatr.domain.DateFakes
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class DateTimeHelperTest {
    private val fixedClock: Clock = DateFakes.fixedClock
    private val timeZone = DateFakes.timeZone
    private val dateTimeHelper = DateTimeHelper(fixedClock, timeZone)

    @Test
    fun calculateDaysAfter_returnsCorrectDate() {
        val date = LocalDate(2024, 1, 1)
        val result = dateTimeHelper.calculateDaysAfter(date, 5)
        assertEquals(LocalDate(2024, 1, 6), result)
    }

    @Test
    fun today_returnsFixedDate() {
        val today = dateTimeHelper.today()
        assertEquals(LocalDate(2024, 1, 1), today)
    }
}
