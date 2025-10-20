package com.hollowvyn.kneatr.domain.model

import androidx.compose.runtime.Immutable
import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import com.hollowvyn.kneatr.domain.util.DateTimeUtils
import kotlinx.datetime.LocalDate

@Immutable
data class Contact(
    val id: Long,
    val name: String,
    val phoneNumber: String, // TODO: support multiple phone numbers later
    val email: String?, // TODO: support multiple emails later
    val tags: Set<ContactTag> = emptySet(),
    val communicationLogs: List<CommunicationLog> = emptyList(),
    val tier: ContactTier? = null,
    val customFrequencyDays: Int? = null,
    val isOverdue: Boolean = false,
    val isDueToday: Boolean = false,
    val nextContactDate: LocalDate? = null,
    val lastDate: LocalDate? = null,
) {
    val lastContactedRelatedDate: RelativeDate?
        get() = lastDate?.let { DateTimeUtils.formatDateRelatively(it) }

    val nextContactDateRelated: RelativeDate?
        get() =
            if (isDueToday) {
                RelativeDate.Today
            } else if (isOverdue) {
                RelativeDate.Overdue
            } else {
                nextContactDate?.let { DateTimeUtils.formatDateRelatively(it) }
            }
}

@Immutable
data class ContactTag(
    val id: Long,
    val name: String,
)

@Immutable
data class CommunicationLog(
    val type: CommunicationType,
    val date: LocalDate,
    val contactId: Long,
    val id: Long? = null,
    val notes: String? = null,
)

@Immutable
data class ContactTier(
    val id: Long,
    val name: String,
    val daysBetweenContact: Int,
)
