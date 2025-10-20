package com.hollowvyn.kneatr.domain.util

import com.hollowvyn.kneatr.domain.DateFakes
import com.hollowvyn.kneatr.domain.model.RelativeDate
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class DateTimeUtilsTest {
    private val fixedClock = DateFakes.fixedClock
    private val utcTimeZone = DateFakes.timeZone

    @Before
    fun setUp() {
        DateTimeUtils.clock = fixedClock
        DateTimeUtils.timeZone = utcTimeZone
        DateTimeUtils.currentSystemTime = fixedClock.now().toEpochMilliseconds()
    }

    @After
    fun tearDown() {
        DateTimeUtils.clock = kotlin.time.Clock.System
        DateTimeUtils.timeZone = TimeZone.currentSystemDefault()
        DateTimeUtils.currentSystemTime = System.currentTimeMillis()
    }

    @Test
    fun `formatDate with LocalDate formats correctly`() {
        val date = LocalDate(2025, 10, 20)
        assertEquals(DateTimeUtils.formatDate(date), "Oct 20, 2025")
    }

    @Test
    fun `formatDate with single digit day formats correctly`() {
        val date = LocalDate(2025, 1, 9)
        assertEquals(DateTimeUtils.formatDate(date), "Jan 09, 2025")
    }

    @Test
    fun `formatDate with millis formats correctly`() {
        val millis = DateTimeUtils.toEpochMillis(LocalDate(1970, 1, 1))
        assertEquals(DateTimeUtils.formatDate(millis), "Jan 01, 1970")
    }

    @Test
    fun `formatPastDate with future date returns Absolute`() {
        val futureDate = LocalDate(2024, 1, 2)
        val result = DateTimeUtils.formatPastDate(futureDate)
        assertTrue(result is RelativeDate.Absolute)
        assertEquals(result.date, futureDate)
    }

    @Test
    fun `formatPastDate with today returns Today`() {
        val today = LocalDate(2024, 1, 1)
        val result = DateTimeUtils.formatPastDate(today)
        assertEquals(result, RelativeDate.Today)
    }

    @Test
    fun `formatPastDate with yesterday returns Yesterday`() {
        val yesterday = LocalDate(2023, 12, 31)
        val result = DateTimeUtils.formatPastDate(yesterday)
        assertEquals(result, RelativeDate.Yesterday)
    }

    //
    @Test
    fun `formatPastDate within last week returns LastWeekday`() {
        val date = LocalDate(2023, 12, 30)
        val result = DateTimeUtils.formatPastDate(date)
        assertTrue(result is RelativeDate.LastWeekday)
        assertEquals(result.dayOfWeek, DayOfWeek.SATURDAY)
    }

    @Test
    fun `formatPastDate one week ago returns WeeksAgo(1)`() {
        val date = LocalDate(2023, 12, 25)
        val result = DateTimeUtils.formatPastDate(date)
        assertTrue(result is RelativeDate.WeeksAgo)
        assertEquals(result.count, 1)
    }

    @Test
    fun `formatFutureDate with past date returns Overdue`() {
        val pastDate = LocalDate(2023, 12, 31)
        val result = DateTimeUtils.formatFutureDate(pastDate)
        assertTrue(result is RelativeDate.Overdue)
    }

    @Test
    fun `formatFutureDate with tomorrow returns Tomorrow`() {
        val tomorrow = LocalDate(2024, 1, 2)
        val result = DateTimeUtils.formatFutureDate(tomorrow)
        assertTrue(result is RelativeDate.Tomorrow)
    }

    @Test
    fun `toLocalDate and toEpochMillis roundtrip`() {
        val date = LocalDate(2024, 7, 19)
        val millis = DateTimeUtils.toEpochMillis(date)
        val convertedDate = DateTimeUtils.toLocalDate(millis)
        assertEquals(convertedDate, date)
    }

    @Test
    fun `getSelectablePastAndPresentDates - past date is selectable`() {
        val selectableDates = DateTimeUtils.getSelectablePastAndPresentDates()
        val pastMillis = fixedClock.now().toEpochMilliseconds() - 10000
        assertTrue(selectableDates.isSelectableDate(pastMillis))
    }

    @Test
    fun `getSelectablePastAndPresentDates - future date is not selectable`() {
        val selectableDates = DateTimeUtils.getSelectablePastAndPresentDates()
        val futureMillis = fixedClock.now().toEpochMilliseconds() + 1000
        assertFalse(selectableDates.isSelectableDate(futureMillis))
    }

    @Test
    fun `getSelectablePastAndPresentDates - future year is not selectable`() {
        val selectableDates = DateTimeUtils.getSelectablePastAndPresentDates()
        val futureYear = DateTimeUtils.today().year.plus(2)
        assertFalse(selectableDates.isSelectableYear(futureYear))
    }

    @Test
    fun `getSelectablePastAndPresentDates - past or present year is selectable`() {
        val selectableDates = DateTimeUtils.getSelectablePastAndPresentDates()
        val futureYear = DateTimeUtils.today().year.minus(1)
        assertTrue(selectableDates.isSelectableYear(futureYear))
    }

    @Test
    fun `calculateDaysAfter - adds days correctly`() {
        val date = LocalDate(2024, 1, 1)
        val newDate = DateTimeUtils.calculateDaysAfter(date, 5)
        assertEquals(newDate, (LocalDate(2024, 1, 6)))
    }
}
