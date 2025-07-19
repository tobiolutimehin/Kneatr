package com.hollowvyn.kneatr.data.local.dao.typeconverter

import com.hollowvyn.kneatr.data.local.typeconverter.LocalDateConverters
import junit.framework.TestCase
import kotlinx.datetime.LocalDate
import org.junit.Test

class LocalDateConvertersTest {
    @Test
    fun `fromEpochDay converts correctly`() {
        val date = LocalDate(2025, 1, 1)
        val epochDay = LocalDateConverters.localDateToEpochDays(date)
        val restored = LocalDateConverters.fromEpochDays(epochDay)
        TestCase.assertEquals(date, restored)
    }

    @Test
    fun `null converts safely`() {
        TestCase.assertEquals(null, LocalDateConverters.localDateToEpochDays(null))
    }

    @Test
    fun `localDateToEpochDays converts correctly`() {
        val date = LocalDate(2025, 1, 1)
        val epochDay = LocalDateConverters.localDateToEpochDays(date)
        TestCase.assertEquals(LocalDateConverters.fromEpochDays(epochDay), date)
    }
}
