package com.hollowvyn.kneatr.domain.model

import androidx.compose.runtime.Immutable
import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import kotlinx.datetime.LocalDate

@Immutable
data class CommunicationLog(
    val type: CommunicationType,
    val date: LocalDate,
    val contactId: Long,
    val id: Long? = null,
    val notes: String? = null,
)
