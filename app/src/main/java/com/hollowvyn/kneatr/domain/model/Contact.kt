package com.hollowvyn.kneatr.domain.model

import androidx.compose.runtime.Immutable
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
) {
    private val lastCommunicationDate = communicationLogs.maxByOrNull { it.date }?.date
    private val nextCommunicationDate: LocalDate? =
        lastCommunicationDate?.let {
            customFrequencyDays?.let { customDays ->
                DateTimeUtils.calculateDaysAfter(it, customDays)
            } ?: tier?.daysBetweenContact?.let { tierDays ->
                DateTimeUtils.calculateDaysAfter(it, tierDays)
            }
        }

    internal val isOverdue = nextCommunicationDate?.let { it < DateTimeUtils.today() } ?: false
    internal val isDueToday = nextCommunicationDate?.let { it == DateTimeUtils.today() } ?: false

    internal val lastCommunicationDateRelative: RelativeDate? =
        lastCommunicationDate?.let { DateTimeUtils.formatPastDate(it) }

    internal val nextCommunicationDateRelative: RelativeDate? =
        if (isDueToday) {
            RelativeDate.Today
        } else if (isOverdue) {
            RelativeDate.Overdue
        } else {
            nextCommunicationDate?.let { DateTimeUtils.formatFutureDate(it) }
        }
}
