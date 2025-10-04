package com.hollowvyn.kneatr.domain.usecase

import com.hollowvyn.kneatr.data.util.DateTimeHelper
import com.hollowvyn.kneatr.domain.model.Contact
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class GetNextContactDateUseCase
    @Inject
    constructor(
        private val dateTimeHelper: DateTimeHelper,
    ) {
        operator fun invoke(contact: Contact): LocalDate? {
            val lastDate = contact.communicationLogs.maxByOrNull { it.date }?.date ?: return null

            contact.customFrequencyDays?.let {
                return dateTimeHelper.calculateDaysAfter(lastDate, it)
            }
            contact.tier?.daysBetweenContact?.let {
                return dateTimeHelper.calculateDaysAfter(lastDate, it)
            }

            return null
        }
    }
