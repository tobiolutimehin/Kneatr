package com.hollowvyn.kneatr.data.util

import com.hollowvyn.kneatr.data.local.entity.relation.ContactWithTagsAndTier
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class ContactExtensions
    @Inject
    constructor(
        private val dateTimeHelper: DateTimeHelper,
    ) {
        fun lastContactedDate(contact: ContactWithTagsAndTier): LocalDate? = contact.communicationLogs.maxByOrNull { it.date }?.date

        @Suppress("ReturnCount")
        fun nextContactDate(contact: ContactWithTagsAndTier): LocalDate? {
            val lastDate = lastContactedDate(contact) ?: return null
            contact.contact.customFrequencyDays?.let {
                return dateTimeHelper.calculateDaysAfter(
                    lastDate,
                    it,
                )
            }
            contact.tier?.daysBetweenContact?.let {
                return dateTimeHelper.calculateDaysAfter(
                    lastDate,
                    it,
                )
            }
            return null
        }

        fun isOverdue(contact: ContactWithTagsAndTier): Boolean =
            nextContactDate(contact)?.let {
                it < dateTimeHelper.today()
            } ?: false
    }
