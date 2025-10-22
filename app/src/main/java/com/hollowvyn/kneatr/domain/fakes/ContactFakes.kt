package com.hollowvyn.kneatr.domain.fakes

import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import com.hollowvyn.kneatr.domain.model.CommunicationLog
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.domain.model.ContactTag
import com.hollowvyn.kneatr.domain.model.ContactTier
import kotlinx.datetime.LocalDate

object ContactFakes {
    val tagFamily = ContactTag(id = 1, name = "Family")
    val tagWork = ContactTag(id = 2, name = "Work")
    val tagFriend = ContactTag(id = 3, name = "Friend")

    val tier1 = ContactTier(id = 1, name = "Tier 1", daysBetweenContact = 14)
    val tier2 = ContactTier(id = 2, name = "Tier 2", daysBetweenContact = 30)
    val tier3 = ContactTier(id = 3, name = "Tier 3", daysBetweenContact = 90)

    val logPhoneCall =
        CommunicationLog(
            id = 1,
            contactId = 1,
            type = CommunicationType.PHONE_CALL,
            date = LocalDate(2024, 1, 15),
            notes = "Discussed the project timeline.",
        )
    val logTextMessage =
        CommunicationLog(
            id = 2,
            contactId = 1,
            type = CommunicationType.MESSAGE,
            date = LocalDate(2024, 2, 1),
            notes = "Confirmed meeting for next week.",
        )
    val logEmail =
        CommunicationLog(
            id = 3,
            contactId = 2,
            type = CommunicationType.EMAIL,
            date = LocalDate(2024, 3, 10),
            notes = "Sent over the quarterly report.",
        )

    val basicContact =
        Contact(
            id = 1,
            name = "Jane Doe",
            phoneNumber = "555-0101",
            email = "jane.doe@email.com",
        )

    val contactWithLogs =
        Contact(
            id = 2,
            name = "John Smith",
            phoneNumber = "555-0102",
            email = "john.smith@email.com",
            communicationLogs = listOf(logPhoneCall, logTextMessage),
        )

    val contactWithTier =
        Contact(
            id = 3,
            name = "Emily White",
            phoneNumber = "555-0103",
            email = "emily.white@email.com",
            tier = tier2,
        )

    val contactWithTags =
        Contact(
            id = 4,
            name = "Michael Brown",
            phoneNumber = "555-0104",
            email = "michael.brown@email.com",
            tags = setOf(tagWork, tagFriend),
        )

    val fullContact =
        Contact(
            id = 5,
            name = "Sarah Green",
            phoneNumber = "555-0105",
            email = "sarah.green@email.com",
            tier = tier1,
            tags = setOf(tagFamily),
            communicationLogs = listOf(logEmail),
            customFrequencyDays = 7,
        )

    val overdueContact =
        Contact(
            id = 6,
            name = "David Black",
            phoneNumber = "555-0106",
            email = "david.black@email.com",
            tier = tier3,
            communicationLogs = listOf(logPhoneCall.copy(date = LocalDate(2023, 1, 1))),
        )

    val allContacts =
        listOf(
            basicContact,
            contactWithLogs,
            contactWithTier,
            contactWithTags,
            fullContact,
            overdueContact,
        )
}
