package com.hollowvyn.kneatr.data.repository

import com.hollowvyn.kneatr.domain.model.CommunicationLogDto
import com.hollowvyn.kneatr.domain.model.ContactDto
import com.hollowvyn.kneatr.domain.model.ContactTagDto
import com.hollowvyn.kneatr.domain.model.ContactTierDto
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface ContactsRepository {
    // Contact CRUD
    suspend fun insertContact(contact: ContactDto)

    suspend fun insertContacts(contacts: List<ContactDto>)

    suspend fun updateContact(contact: ContactDto)

    fun getAllContacts(): Flow<List<ContactDto>>

    fun getContactsByTierId(tierId: Long): Flow<List<ContactDto>>

    fun searchContactsByNamePhoneOrEmail(query: String): Flow<List<ContactDto>>

    fun getContactById(id: Long): Flow<ContactDto?>

    // Contact Tag CrossRef operations
    suspend fun addTagToContact(
        contactId: Long,
        tagId: Long,
    )

    suspend fun removeTagFromContact(
        contactId: Long,
        tagId: Long,
    )

    suspend fun removeAllTagsFromContact(contactId: Long)

    // Tags
    fun getAllTags(): Flow<Set<ContactTagDto>>

    fun getTagsWithContacts(): Flow<List<Pair<ContactTagDto, List<ContactDto>>>>

    fun getTagWithContactsById(id: Long): Flow<List<ContactDto>?>

    suspend fun insertTag(tag: ContactTagDto)

    suspend fun updateTag(tag: ContactTagDto)

    suspend fun deleteTag(tag: ContactTagDto)

    // Tiers
    suspend fun insertTier(tier: ContactTierDto)

    suspend fun insertTiers(tiers: List<ContactTierDto>)

    suspend fun updateTier(tier: ContactTierDto)

    fun getAllTiers(): Flow<List<ContactTierDto>>

    fun getTierById(id: Long): Flow<ContactTierDto?>

    suspend fun deleteAllTiers()

    // Communication Logs
    suspend fun insertCommunicationLog(log: CommunicationLogDto)

    fun getCommunicationLogsForContact(contactId: Long): Flow<List<CommunicationLogDto>>

    suspend fun updateCommunicationLog(log: CommunicationLogDto)

    suspend fun deleteCommunicationLog(log: CommunicationLogDto)

    suspend fun deleteCommunicationLogsForContact(contactId: Long)

    suspend fun deleteAllCommunicationLogs()

    fun getAllCommunicationLogs(): Flow<List<CommunicationLogDto>>

    fun getCommunicationLogsByDate(date: LocalDate): Flow<List<CommunicationLogDto>>
}
