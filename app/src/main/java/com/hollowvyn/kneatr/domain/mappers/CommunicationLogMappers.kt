package com.hollowvyn.kneatr.domain.mappers

import com.hollowvyn.kneatr.data.local.entity.CommunicationLogEntity
import com.hollowvyn.kneatr.domain.model.CommunicationLogDto

fun CommunicationLogDto.toEntity(): CommunicationLogEntity =
    CommunicationLogEntity(
        communicationId = id,
        type = type,
        date = date,
        contactId = contactId,
        notes = notes,
    )

fun CommunicationLogEntity.toModel(): CommunicationLogDto =
    CommunicationLogDto(
        id = communicationId,
        type = type,
        date = date,
        notes = notes,
        contactId = contactId,
    )

fun List<CommunicationLogEntity>.toModelList(): List<CommunicationLogDto> = map { it.toModel() }
