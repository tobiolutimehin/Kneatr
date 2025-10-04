package com.hollowvyn.kneatr.domain.model

import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import kotlinx.datetime.LocalDate

data class Contact(
    val id: Long,
    val name: String,
    val phoneNumber: String, // TODO: support multiple phone numbers later
    val email: String?, // TODO: support multiple emails later
    val tags: Set<ContactTag> = emptySet(),
    val communicationLogs: List<CommunicationLog> = emptyList(),
    val tier: ContactTier? = null,
    val customFrequencyDays: Int? = null,
)

data class ContactTag(
    val id: Long,
    val name: String,
)

data class CommunicationLog(
    val id: Long,
    val type: CommunicationType,
    val date: LocalDate,
    val contactId: Long,
    val notes: String? = null,
)

data class ContactTier(
    val id: Long,
    val name: String,
    val daysBetweenContact: Int,
)
