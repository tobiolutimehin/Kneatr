package com.hollowvyn.kneatr.data.local.util

import com.hollowvyn.kneatr.data.local.entity.CommunicationLogEntity
import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import com.hollowvyn.kneatr.data.local.entity.ContactEntity
import com.hollowvyn.kneatr.data.local.entity.ContactTierEntity
import com.hollowvyn.kneatr.data.local.entity.relation.ContactWithDetails
import com.hollowvyn.kneatr.data.util.ContactExtensions
import com.hollowvyn.kneatr.data.util.DateTimeHelper
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class ContactExtensionsTest {
    private val dateTimeHelper = DateMocks.dateTimeHelper
    private val contactExtensions = ContactExtensions(dateTimeHelper)

    private val baseContact =
        ContactEntity(
            contactId = 1,
            name = "Test User",
            phoneNumber = "12345",
        )

    @Test
    fun lastContactedDate_returnsMostRecentDate() {
        val logs =
            listOf(
                CommunicationLogEntity(
                    1,
                    CommunicationType.PHONE_CALL,
                    LocalDate(2023, 12, 1),
                    1,
                    null,
                ),
                CommunicationLogEntity(2, CommunicationType.EMAIL, LocalDate(2023, 12, 5), 1, null),
            )
        val contactWithLogs = ContactWithDetails(baseContact, emptyList(), null, logs)
        val result = contactExtensions.lastContactedDate(contactWithLogs)
        assertEquals(LocalDate(2023, 12, 5), result)
    }

    @Test
    fun nextContactDate_returnsNull_whenNoLogs() {
        val contactWithNoLogs = ContactWithDetails(baseContact, emptyList(), null, emptyList())
        val result = contactExtensions.nextContactDate(contactWithNoLogs)
        assertNull(result)
    }

    @Test
    fun nextContactDate_usesCustomFrequency() {
        val contact = baseContact.copy(customFrequencyDays = 10)
        val logs =
            listOf(
                CommunicationLogEntity(
                    1,
                    CommunicationType.PHONE_CALL,
                    LocalDate(2023, 12, 1),
                    1,
                    null,
                ),
            )
        val contactWithLogs = ContactWithDetails(contact, emptyList(), null, logs)
        val result = contactExtensions.nextContactDate(contactWithLogs)
        assertEquals(LocalDate(2023, 12, 11), result)
    }

    @Test
    fun nextContactDate_usesTierFrequency_whenNoCustom() {
        val tier = ContactTierEntity(1, "Tier 1", 7)
        val logs =
            listOf(
                CommunicationLogEntity(
                    1,
                    CommunicationType.PHONE_CALL,
                    LocalDate(2023, 12, 1),
                    1,
                    null,
                ),
            )
        val contactWithLogs = ContactWithDetails(baseContact, emptyList(), tier, logs)
        val result = contactExtensions.nextContactDate(contactWithLogs)
        assertEquals(LocalDate(2023, 12, 8), result)
    }

    @Test
    fun isOverdue_returnsTrue_whenNextContactBeforeToday() {
        val contact = baseContact.copy(customFrequencyDays = 10)
        val logs =
            listOf(
                CommunicationLogEntity(
                    1,
                    CommunicationType.PHONE_CALL,
                    LocalDate(2023, 12, 1),
                    1,
                    null,
                ),
            )
        val contactWithLogs = ContactWithDetails(contact, emptyList(), null, logs)
        val result = contactExtensions.isOverdue(contactWithLogs)
        assertTrue(result)
    }

    @Test
    fun isOverdue_returnsFalse_whenNextContactAfterToday() {
        val contact = baseContact.copy(customFrequencyDays = 10)
        val logs =
            listOf(
                CommunicationLogEntity(
                    1,
                    CommunicationType.PHONE_CALL,
                    LocalDate(2023, 12, 31),
                    1,
                    null,
                ),
            )
        val contactWithLogs = ContactWithDetails(contact, emptyList(), null, logs)
        val result = contactExtensions.isOverdue(contactWithLogs)
        assertFalse(result)
    }

    @Test
    fun isOverdue_returnsFalse_whenNoNextContact() {
        val contactWithNoLogs = ContactWithDetails(baseContact, emptyList(), null, emptyList())
        val result = contactExtensions.isOverdue(contactWithNoLogs)
        assertFalse(result)
    }
}

@OptIn(ExperimentalTime::class)
object DateMocks {
    class FixedClock(
        private val fixedInstant: Instant,
    ) : Clock {
        override fun now(): Instant = fixedInstant
    }

    @Suppress("ktlint:standard:function-naming")
    private fun Clock.Companion.Fixed(fixedInstant: Instant): Clock = FixedClock(fixedInstant)

    val fixedClock: Clock = Clock.Fixed(Instant.parse("2024-01-01T00:00:00Z"))

    val timeZone = TimeZone.UTC
    val dateTimeHelper = DateTimeHelper(fixedClock, timeZone)
}
