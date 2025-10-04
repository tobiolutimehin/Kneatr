package com.hollowvyn.kneatr.data.repository

import com.hollowvyn.kneatr.data.local.dao.CommunicationLogDao
import com.hollowvyn.kneatr.data.local.dao.ContactDao
import com.hollowvyn.kneatr.data.local.dao.ContactTagCrossRefDao
import com.hollowvyn.kneatr.data.local.dao.ContactTagDao
import com.hollowvyn.kneatr.data.local.dao.ContactTierDao
import com.hollowvyn.kneatr.data.local.entity.crossRef.ContactTagCrossRef
import com.hollowvyn.kneatr.data.remote.ContactFetcher
import com.hollowvyn.kneatr.data.util.DateTimeHelper
import com.hollowvyn.kneatr.domain.mappers.toEntity
import com.hollowvyn.kneatr.domain.mappers.toListEntity
import com.hollowvyn.kneatr.domain.mappers.toListModel
import com.hollowvyn.kneatr.domain.mappers.toModel
import com.hollowvyn.kneatr.domain.mappers.toModelSet
import com.hollowvyn.kneatr.domain.model.CommunicationLog
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.domain.model.ContactTag
import com.hollowvyn.kneatr.domain.model.ContactTier
import com.hollowvyn.kneatr.domain.repository.ContactsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class ContactsRepositoryImpl
    @Inject
    constructor(
        private val contactDao: ContactDao,
        private val contactTagCrossRefDao: ContactTagCrossRefDao,
        private val contactTagDao: ContactTagDao,
        private val contactTierDao: ContactTierDao,
        private val communicationLogDao: CommunicationLogDao,
        private val contactFetcher: ContactFetcher,
        private val dateTimeHelper: DateTimeHelper,
    ) : ContactsRepository {
        // Contact operations
        override suspend fun insertContact(contact: Contact) =
            contact.toEntity().let { contactEntity ->
                contactDao.insertContact(contactEntity)
            }

        override suspend fun insertContacts(contacts: List<Contact>) = contactDao.insertContacts(contacts.toListEntity())

        override suspend fun updateContact(contact: Contact) = contact.toEntity().let { contactDao.updateContact(it) }

        override fun getAllContacts(): Flow<List<Contact>> =
            contactDao.getAllContacts().map { contacts -> contacts.toListModel(dateTimeHelper) }

        override fun getContactsByTierId(tierId: Long): Flow<List<Contact>> =
            contactDao
                .getContactsByTierId(
                    tierId,
                ).transform {
                    it.toListModel(dateTimeHelper)
                }

        override fun searchContactsByNamePhoneOrEmail(query: String): Flow<List<Contact>> =
            contactDao.searchContactsByNamePhoneOrEmail(query).map { it.toListModel(dateTimeHelper) }

        override fun getContactById(id: Long): Flow<Contact?> = contactDao.getContactById(id).map { it?.toModel(dateTimeHelper) }

        // Tag CrossRef operations
        override suspend fun addTagToContact(
            contactId: Long,
            tagId: Long,
        ) = contactTagCrossRefDao.addTagToContact(ContactTagCrossRef(contactId, tagId))

        override suspend fun removeTagFromContact(
            contactId: Long,
            tagId: Long,
        ) = contactTagCrossRefDao.removeTagFromContact(ContactTagCrossRef(contactId, tagId))

        override suspend fun removeAllTagsFromContact(contactId: Long) =
            contactTagCrossRefDao.removeAllTagsFromContact(
                contactId,
            )

        // Tag operations
        override fun getAllTags(): Flow<Set<ContactTag>> = contactTagDao.getAllTags().transform { it.toModelSet() }

        override fun getTagsWithContacts(): Flow<List<Pair<ContactTag, List<Contact>>>> =
            contactTagDao
                .getAllTags()
                .flatMapLatest { tags ->
                    val flowsPerTag: List<Flow<Pair<ContactTag, List<Contact>>>> =
                        tags.map { tag ->
                            contactDao
                                .getContactsByTagId(tag.tagId)
                                .map { contacts ->
                                    tag.toModel() to contacts.map { it.toModel(dateTimeHelper) }
                                }
                        }
                    combine(flowsPerTag) { pairsArray -> pairsArray.toList() }
                }

        override fun getTagWithContactsById(id: Long): Flow<List<Contact>?> =
            contactDao.getContactsByTagId(id).transform {
                it.toListModel(dateTimeHelper)
            }

        override suspend fun insertTag(tag: ContactTag) = contactTagDao.insertTag(tag.toEntity())

        override suspend fun updateTag(tag: ContactTag) = contactTagDao.updateTag(tag.toEntity())

        override suspend fun deleteTag(tag: ContactTag) = contactTagDao.deleteTag(tag.toEntity())

        // Tier operations
        override suspend fun insertTier(tier: ContactTier) = contactTierDao.insertTier(tier.toEntity())

        override suspend fun insertTiers(tiers: List<ContactTier>) = contactTierDao.insertTiers(tiers.map { it.toEntity() })

        override suspend fun updateTier(tier: ContactTier) = contactTierDao.updateTier(tier.toEntity())

        override fun getAllTiers(): Flow<List<ContactTier>> =
            contactTierDao.getAllTiers().transform { tiers ->
                tiers.map { it.toModel() }
            }

        override fun getTierById(id: Long): Flow<ContactTier?> =
            contactTierDao.getTierById(id).transform {
                it?.toModel()
            }

        override suspend fun deleteAllTiers() = contactTierDao.deleteAllTiers()

        // Communication Logs operations
        override suspend fun insertCommunicationLog(log: CommunicationLog) = communicationLogDao.insertCommunicationLog(log.toEntity())

        override fun getCommunicationLogsForContact(contactId: Long): Flow<List<CommunicationLog>> =
            communicationLogDao.getCommunicationLogsForContact(contactId).transform { logs ->
                logs.map { it.toModel() }
            }

        override suspend fun updateCommunicationLog(log: CommunicationLog) =
            communicationLogDao.updateCommunicationLog(
                log.toEntity(),
            )

        override suspend fun deleteCommunicationLog(log: CommunicationLog) =
            communicationLogDao.deleteCommunicationLog(
                log.toEntity(),
            )

        override suspend fun deleteCommunicationLogsForContact(contactId: Long) =
            communicationLogDao.deleteCommunicationLogsForContact(contactId)

        override suspend fun deleteAllCommunicationLogs() = communicationLogDao.deleteAllCommunicationLogs()

        override fun getAllCommunicationLogs(): Flow<List<CommunicationLog>> =
            communicationLogDao.getAllCommunicationLogs().transform { logs ->
                logs.map { it.toModel() }
            }

        override fun getCommunicationLogsByDate(date: LocalDate): Flow<List<CommunicationLog>> =
            communicationLogDao.getCommunicationLogsByDate(date).map { logs ->
                logs.map { it.toModel() }
            }

        override suspend fun syncContacts() {
            coroutineScope {
                val phoneContacts = contactFetcher.fetchContacts()
                val dbContacts = contactDao.getAllContactsAtOnce()

                val phoneContactIds = phoneContacts.map { it.id }.toSet()
                val dbContactIds = dbContacts.map { it.contact.contactId }.toSet()

                // Contacts to add or update
                val toUpsert =
                    phoneContacts.mapNotNull { phoneContact ->
                        val matchingDbContact =
                            dbContacts.find { it.contact.contactId == phoneContact.id }
                        if (matchingDbContact == null) {
                            phoneContact.toEntity()
                        } else {
                            // Existing contact, check if update needed
                            val dbEntity = matchingDbContact.contact
                            if (
                                dbEntity.name != phoneContact.name ||
                                dbEntity.phoneNumber != phoneContact.phoneNumber ||
                                dbEntity.email != phoneContact.email
                            ) {
                                phoneContact.toEntity() // updated entity
                            } else {
                                null // no change needed
                            }
                        }
                    }

                // Contacts to delete (in DB but not on phone)
                val toDeleteIds = dbContactIds.subtract(phoneContactIds)

                // Perform DB operations
                contactDao.insertContacts(toUpsert)
                toDeleteIds.forEach { id -> contactDao.deleteContactById(id) }

                // Return fresh contacts including tiers/tags/etc
                contactDao.getAllContactsAtOnce().map { it.toModel(dateTimeHelper) }
            }
        }
    }
