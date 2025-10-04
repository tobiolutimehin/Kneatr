package com.hollowvyn.kneatr.domain.usecase

import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import com.hollowvyn.kneatr.data.local.util.DateMocks
import com.hollowvyn.kneatr.domain.model.CommunicationLog
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.domain.model.ContactTier
import kotlinx.datetime.LocalDate
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class IsContactOverdueUseCaseTest {
    private val getNextContactDate = GetNextContactDateUseCase(DateMocks.dateTimeHelper)
    private lateinit var isContactOverdue: IsContactOverdueUseCase

    private val dateTimeHelper = DateMocks.dateTimeHelper

    private val baseContact =
        Contact(
            id = 2,
            name = "Jane Smith",
            phoneNumber = "987-654-3210",
            email = "jane.smith@example.com",
        )

    @Before
    fun setup() {
        isContactOverdue = IsContactOverdueUseCase(getNextContactDate, DateMocks.dateTimeHelper)
    }

    @Test
    fun `invoke returns true when next contact date is in the past`() {
        val communicationLogs =
            listOf(
                CommunicationLog(1, CommunicationType.PHONE_CALL, LocalDate(2023, 12, 1), 1),
            )
        val contact =
            baseContact.copy(
                communicationLogs = communicationLogs,
                tier =
                    ContactTier(
                        1,
                        "Tier 1",
                        7,
                    ),
            )
        val result = isContactOverdue(contact)

        assertTrue(result)
    }

    @Test
    fun `invoke returns false when next contact date is in the future`() {
        val communicationLogs =
            listOf(
                CommunicationLog(1, CommunicationType.PHONE_CALL, dateTimeHelper.today(), 1),
            )
        val contact =
            baseContact.copy(
                communicationLogs = communicationLogs,
                tier =
                    ContactTier(
                        1,
                        "Tier 1",
                        7,
                    ),
            )
        val result = isContactOverdue(contact)

        assertFalse(result)
    }

    @Test
    fun `invoke returns false when next contact date is today`() {
        val communicationLogs =
            listOf(
                CommunicationLog(1, CommunicationType.PHONE_CALL, dateTimeHelper.today(), 1),
            )
        val contact =
            baseContact.copy(
                communicationLogs = communicationLogs,
                tier =
                    ContactTier(
                        1,
                        "Tier 1",
                        0,
                    ),
            )
        val result = isContactOverdue(contact)
        assertFalse(result)
    }

    @Test
    fun `invoke returns false when there is no next contact date`() {
        val result = isContactOverdue(baseContact)

        assertFalse(result)
    }
}
