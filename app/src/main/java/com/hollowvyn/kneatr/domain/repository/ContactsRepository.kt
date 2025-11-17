package com.hollowvyn.kneatr.domain.repository

import com.hollowvyn.kneatr.domain.model.CommunicationLog
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.domain.model.ContactTag
import com.hollowvyn.kneatr.domain.model.ContactTier
import com.hollowvyn.kneatr.domain.model.RelativeDate
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface ContactsRepository {
    // Contact CRUD
    suspend fun insertContact(contact: Contact)

    suspend fun insertContacts(contacts: List<Contact>)

    suspend fun updateContact(contact: Contact)

    fun getAllContacts(): Flow<List<Contact>>

    fun getOverdueContacts(): Flow<List<Contact>>

    fun getUpcomingContacts(relativeDate: RelativeDate.Weeks): Flow<List<Contact>>

    fun getRandomHomeContacts(forceRefresh: Boolean = false): Flow<List<Contact>>

    fun getContactsDueToday(): Flow<List<Contact>>

    fun getContactsByTierId(tierId: Long): Flow<List<Contact>>

    fun searchContactsByNamePhoneOrEmail(query: String): Flow<List<Contact>>

    fun getContactById(id: Long): Flow<Contact?>

    // Contact Tag CrossRef operations
    suspend fun addTagToContact(
        contactId: Long,
        tagId: Long,
    )

    suspend fun addTagsToContact(
        contactId: Long,
        tagIds: List<ContactTag>,
    )

    suspend fun removeTagFromContact(
        contactId: Long,
        tagId: Long,
    )

    suspend fun removeAllTagsFromContact(contactId: Long)

    // Tags
    fun getAllTags(): Flow<Set<ContactTag>>

    fun getTagsWithContacts(): Flow<List<Pair<ContactTag, List<Contact>>>>

    fun getTagWithContactsById(id: Long): Flow<List<Contact>?>

    suspend fun insertTag(tag: ContactTag)

    suspend fun updateTag(tag: ContactTag)

    suspend fun deleteTag(tag: ContactTag)

    // Tiers
    suspend fun insertTier(tier: ContactTier)

    suspend fun insertTiers(tiers: List<ContactTier>)

    suspend fun updateTier(tier: ContactTier)

    fun getAllTiers(): Flow<List<ContactTier>>

    fun getTierById(id: Long): Flow<ContactTier?>

    suspend fun deleteAllTiers()

    // Communication Logs
    suspend fun insertCommunicationLog(log: CommunicationLog)

    fun getCommunicationLogsForContact(contactId: Long): Flow<List<CommunicationLog>>

    suspend fun updateCommunicationLog(log: CommunicationLog)

    suspend fun deleteCommunicationLog(log: CommunicationLog)

    suspend fun deleteCommunicationLogsForContact(contactId: Long)

    suspend fun deleteAllCommunicationLogs()

    fun getAllCommunicationLogs(): Flow<List<CommunicationLog>>

    fun getCommunicationLogsByDate(date: LocalDate): Flow<List<CommunicationLog>>

    suspend fun syncContacts()
}
