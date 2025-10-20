package com.hollowvyn.kneatr.domain

import kotlinx.datetime.TimeZone
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
object DateFakes {
    class FixedClock(
        private val fixedInstant: Instant,
    ) : Clock {
        override fun now(): Instant = fixedInstant
    }

    private fun Clock.Companion.fixed(fixedInstant: Instant): Clock = FixedClock(fixedInstant)

    val fixedClock: Clock = Clock.Companion.fixed(Instant.Companion.parse("2024-01-01T00:00:00Z"))

    val timeZone = TimeZone.Companion.UTC
}
