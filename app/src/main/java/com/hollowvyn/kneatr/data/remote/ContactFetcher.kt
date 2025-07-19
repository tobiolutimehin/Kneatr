package com.hollowvyn.kneatr.data.remote

import com.hollowvyn.kneatr.domain.model.ContactDto

interface ContactFetcher {
    suspend fun fetchContacts(): List<ContactDto>
}
