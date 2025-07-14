package com.hollowvyn.kneatr.data.local.dao

import com.hollowvyn.kneatr.data.local.typeconverter.LocalDateConverters
import junit.framework.TestCase.assertEquals
import kotlinx.datetime.LocalDate
import org.junit.Test

class LocalDateConvertersTest {
    @Test
    fun `fromEpochDay converts correctly`() {
        val date = LocalDate(2025, 1, 1)
        val epochDay = LocalDateConverters.localDateToEpochDays(date)
        val restored = LocalDateConverters.fromEpochDays(epochDay)
        assertEquals(date, restored)
    }

    @Test
    fun `null converts safely`() {
        assertEquals(null, LocalDateConverters.localDateToEpochDays(null))
        assertEquals(null, LocalDateConverters.localDateToEpochDays(null))
    }
}
