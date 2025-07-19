package com.hollowvyn.kneatr.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity(tableName = "communication_logs")
data class CommunicationLog(
    @PrimaryKey(autoGenerate = true) val communicationId: Int,
    val type: CommunicationType,
    val date: LocalDate,
    val contactId: Int,
    val notes: String? = null,
)
