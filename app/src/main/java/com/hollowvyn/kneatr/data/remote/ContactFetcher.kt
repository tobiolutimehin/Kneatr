package com.hollowvyn.kneatr.data.remote

import com.hollowvyn.kneatr.domain.model.Contact

interface ContactFetcher {
    suspend fun fetchContacts(): List<Contact>
}
