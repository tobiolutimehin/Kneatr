package com.hollowvyn.kneatr.domain.mappers

import com.hollowvyn.kneatr.data.local.entity.ContactEntity
import com.hollowvyn.kneatr.data.local.entity.relation.ContactWithDetails
import com.hollowvyn.kneatr.domain.model.Contact

fun Contact.toEntity(): ContactEntity =
    ContactEntity(
        contactId = id,
        name = name,
        phoneNumber = phoneNumber,
        email = email,
        tierId = tier?.id,
        customFrequencyDays = customFrequencyDays,
    )

fun ContactWithDetails.toModel(): Contact =
    Contact(
        id = contact.contactId,
        name = contact.name,
        phoneNumber = contact.phoneNumber,
        email = contact.email,
        tags = tags.toModelSet(),
        communicationLogs = communicationLogs.toModelList(),
        tier = tier?.toModel(),
        customFrequencyDays = contact.customFrequencyDays,
    )

fun List<Contact>.toListEntity() = map { it.toEntity() }

fun List<ContactWithDetails>.toListModel() = map { it.toModel() }
