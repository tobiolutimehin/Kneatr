package com.hollowvyn.kneatr.domain.mappers

import com.hollowvyn.kneatr.data.local.entity.ContactEntity
import com.hollowvyn.kneatr.data.local.entity.relation.ContactWithDetails
import com.hollowvyn.kneatr.domain.model.ContactDto

fun ContactDto.toEntity(): ContactEntity =
    ContactEntity(
        contactId = id,
        name = name,
        phoneNumber = phoneNumber,
        email = email,
        customFrequencyDays = customFrequencyDays,
    )

fun ContactWithDetails.toModel(): ContactDto =
    ContactDto(
        id = contact.contactId,
        name = contact.name,
        phoneNumber = contact.phoneNumber,
        email = contact.email,
        tags = tags.toModelSet(),
        communicationLogs = communicationLogs.toModelList(),
        tier = tier?.toModel(),
        customFrequencyDays = contact.customFrequencyDays,
    )
