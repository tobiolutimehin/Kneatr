package com.hollowvyn.kneatr.domain.model

import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import kotlinx.datetime.LocalDate

data class ContactDto(
    val id: Long,
    val name: String,
    val phoneNumber: String, // TODO: support multiple phone numbers later
    val email: String?, // TODO: support multiple emails later
    val tags: Set<ContactTagDto> = emptySet(),
    val communicationLogs: List<CommunicationLogDto> = emptyList(),
    val tier: ContactTierDto? = null,
    val customFrequencyDays: Int? = null,
)

data class ContactTagDto(
    val id: Long,
    val name: String,
)

data class CommunicationLogDto(
    val id: Long,
    val type: CommunicationType,
    val date: LocalDate,
    val contactId: Long,
    val notes: String? = null,
)

data class ContactTierDto(
    val id: Long,
    val name: String,
    val daysBetweenContact: Int,
)
