package com.hollowvyn.kneatr.domain.mappers

import com.hollowvyn.kneatr.data.local.entity.ContactEntity
import com.hollowvyn.kneatr.data.local.entity.relation.ContactWithDetails
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.domain.util.DateTimeUtils
import kotlinx.datetime.LocalDate

fun Contact.toEntity(): ContactEntity =
    ContactEntity(
        contactId = id,
        name = name,
        phoneNumber = phoneNumber,
        email = email,
        tierId = tier?.id,
        customFrequencyDays = customFrequencyDays,
    )

fun List<Contact>.toListEntity() = map { it.toEntity() }

fun ContactWithDetails.toModel(): Contact {
    val model =
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

    val lastDate = model.communicationLogs.maxByOrNull { it.date }?.date
    val nextDate: LocalDate? =
        lastDate?.let {
            model.customFrequencyDays?.let { customDays ->
                DateTimeUtils.calculateDaysAfter(it, customDays)
            } ?: model.tier?.daysBetweenContact?.let { tierDays ->
                DateTimeUtils.calculateDaysAfter(it, tierDays)
            }
        }

    val isOverdue = nextDate?.let { it < DateTimeUtils.today() } ?: false
    val isDueToday = nextDate?.let { it == DateTimeUtils.today() } ?: false

    return model.copy(
        nextContactDate = nextDate,
        isOverdue = isOverdue,
        isDueToday = isDueToday,
        lastDate = lastDate,
    )
}

fun List<ContactWithDetails>.toListModel(): List<Contact> = map { it.toModel() }
