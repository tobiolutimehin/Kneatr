package com.hollowvyn.kneatr.data.util

import com.hollowvyn.kneatr.data.local.entity.relation.ContactWithDetails
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class ContactExtensions
    @Inject
    constructor(
        private val dateTimeHelper: DateTimeHelper,
    ) {
        fun lastContactedDate(contact: ContactWithDetails): LocalDate? = contact.communicationLogEntities.maxByOrNull { it.date }?.date

        @Suppress("ReturnCount")
        fun nextContactDate(contact: ContactWithDetails): LocalDate? {
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

    fun isOverdue(contact: ContactWithDetails): Boolean =
            nextContactDate(contact)?.let {
                it < dateTimeHelper.today()
            } ?: false
    }
