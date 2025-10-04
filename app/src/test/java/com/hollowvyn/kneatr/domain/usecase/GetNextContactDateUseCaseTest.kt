package com.hollowvyn.kneatr.domain.usecase

import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import com.hollowvyn.kneatr.data.local.util.DateMocks
import com.hollowvyn.kneatr.domain.model.CommunicationLog
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.domain.model.ContactTier
import kotlinx.datetime.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GetNextContactDateUseCaseTest {
    private val getNextContactDate = GetNextContactDateUseCase(DateMocks.dateTimeHelper)

    private val baseContact =
        Contact(
            id = 2,
            name = "Jane Smith",
            phoneNumber = "987-654-3210",
            email = "jane.smith@example.com",
        )

    @Test
    fun `invoke returns null when contact has no communication logs`() {
        val result = getNextContactDate(baseContact)
        assertNull(result)
    }

    @Test
    fun `invoke uses custom frequency when available`() {
        val contact = baseContact.copy(customFrequencyDays = 10)
        val logs =
            listOf(
                CommunicationLog(1, CommunicationType.PHONE_CALL, LocalDate(2023, 12, 1), 1),
            )
        val result = getNextContactDate(contact.copy(communicationLogs = logs))
        assertEquals(LocalDate(2023, 12, 11), result)
    }

    @Test
    fun `invoke uses tier frequency when custom is not available`() {
        val tier = ContactTier(1, "Tier 1", 7)
        val contact = baseContact.copy(tier = tier)
        val logs =
            listOf(
                CommunicationLog(1, CommunicationType.PHONE_CALL, LocalDate(2023, 12, 1), 1),
            )
        val result = getNextContactDate(contact.copy(communicationLogs = logs))
        assertEquals(LocalDate(2023, 12, 8), result)
    }

    @Test
    fun `invoke returns null when no frequency is defined`() {
        val logs =
            listOf(
                CommunicationLog(1, CommunicationType.PHONE_CALL, LocalDate(2023, 12, 1), 1),
            )
        val result = getNextContactDate(baseContact.copy(communicationLogs = logs))
        assertNull(result)
    }
}
