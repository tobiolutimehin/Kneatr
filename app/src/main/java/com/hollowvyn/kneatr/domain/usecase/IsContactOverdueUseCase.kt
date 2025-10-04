package com.hollowvyn.kneatr.domain.usecase

import com.hollowvyn.kneatr.data.util.DateTimeHelper
import com.hollowvyn.kneatr.domain.model.Contact
import javax.inject.Inject

class IsContactOverdueUseCase
    @Inject
    constructor(
        private val getNextContactDate: GetNextContactDateUseCase,
        private val dateTimeHelper: DateTimeHelper,
    ) {
        operator fun invoke(contact: Contact): Boolean =
            getNextContactDate(contact)?.let { nextDate ->
                nextDate < dateTimeHelper.today()
            } ?: false
    }
