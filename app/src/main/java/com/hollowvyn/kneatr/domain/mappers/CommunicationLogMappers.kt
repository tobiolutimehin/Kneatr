package com.hollowvyn.kneatr.domain.mappers

import com.hollowvyn.kneatr.data.local.entity.CommunicationLogEntity
import com.hollowvyn.kneatr.domain.model.CommunicationLog

fun CommunicationLog.toEntity(): CommunicationLogEntity =
    CommunicationLogEntity(
        communicationId = id ?: 0,
        type = type,
        date = date,
        contactId = contactId,
        notes = notes,
    )

fun CommunicationLogEntity.toModel(): CommunicationLog =
    CommunicationLog(
        id = communicationId,
        type = type,
        date = date,
        notes = notes,
        contactId = contactId,
    )

fun List<CommunicationLogEntity>.toModelList(): List<CommunicationLog> = map { it.toModel() }.sortedByDescending { it.date }
