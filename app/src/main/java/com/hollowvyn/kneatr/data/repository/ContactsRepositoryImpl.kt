package com.hollowvyn.kneatr.data.repository

import com.hollowvyn.kneatr.data.local.dao.CommunicationLogDao
import com.hollowvyn.kneatr.data.local.dao.ContactDao
import com.hollowvyn.kneatr.data.local.dao.ContactTagCrossRefDao
import com.hollowvyn.kneatr.data.local.dao.ContactTagDao
import com.hollowvyn.kneatr.data.local.dao.ContactTierDao
import com.hollowvyn.kneatr.data.local.entity.crossRef.ContactTagCrossRef
import com.hollowvyn.kneatr.data.remote.ContactFetcher
import com.hollowvyn.kneatr.domain.mappers.toEntity
import com.hollowvyn.kneatr.domain.mappers.toListEntity
import com.hollowvyn.kneatr.domain.mappers.toListModel
import com.hollowvyn.kneatr.domain.mappers.toModel
import com.hollowvyn.kneatr.domain.mappers.toModelSet
import com.hollowvyn.kneatr.domain.model.CommunicationLogDto
import com.hollowvyn.kneatr.domain.model.ContactDto
import com.hollowvyn.kneatr.domain.model.ContactTagDto
import com.hollowvyn.kneatr.domain.model.ContactTierDto
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
    ) : ContactsRepository {
        // Contact operations
        override suspend fun insertContact(contact: ContactDto) =
            contact.toEntity().let { contactEntity ->
                contactDao.insertContact(contactEntity)
            }

        override suspend fun insertContacts(contacts: List<ContactDto>) = contactDao.insertContacts(contacts.toListEntity())

        override suspend fun updateContact(contact: ContactDto) = contact.toEntity().let { contactDao.updateContact(it) }

        override fun getAllContacts(): Flow<List<ContactDto>> = contactDao.getAllContacts().map { contacts -> contacts.toListModel() }

        override fun getContactsByTierId(tierId: Long): Flow<List<ContactDto>> =
            contactDao
                .getContactsByTierId(
                    tierId,
                ).transform {
                    it.toListModel()
                }

        override fun searchContactsByNamePhoneOrEmail(query: String): Flow<List<ContactDto>> =
            contactDao.searchContactsByNamePhoneOrEmail(query).map { it.toListModel() }

        override fun getContactById(id: Long): Flow<ContactDto?> = contactDao.getContactById(id).transform { it?.toModel() }

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
        override fun getAllTags(): Flow<Set<ContactTagDto>> = contactTagDao.getAllTags().transform { it.toModelSet() }

        override fun getTagsWithContacts(): Flow<List<Pair<ContactTagDto, List<ContactDto>>>> =
            contactTagDao
                .getAllTags()
                .flatMapLatest { tags ->
                    val flowsPerTag: List<Flow<Pair<ContactTagDto, List<ContactDto>>>> =
                        tags.map { tag ->
                            contactDao
                                .getContactsByTagId(tag.tagId)
                                .map { contacts ->
                                    tag.toModel() to contacts.map { it.toModel() }
                                }
                        }
                    combine(flowsPerTag) { pairsArray -> pairsArray.toList() }
                }

        override fun getTagWithContactsById(id: Long): Flow<List<ContactDto>?> =
            contactDao.getContactsByTagId(id).transform {
                it.toListModel()
            }

        override suspend fun insertTag(tag: ContactTagDto) = contactTagDao.insertTag(tag.toEntity())

        override suspend fun updateTag(tag: ContactTagDto) = contactTagDao.updateTag(tag.toEntity())

        override suspend fun deleteTag(tag: ContactTagDto) = contactTagDao.deleteTag(tag.toEntity())

        // Tier operations
        override suspend fun insertTier(tier: ContactTierDto) = contactTierDao.insertTier(tier.toEntity())

        override suspend fun insertTiers(tiers: List<ContactTierDto>) = contactTierDao.insertTiers(tiers.map { it.toEntity() })

        override suspend fun updateTier(tier: ContactTierDto) = contactTierDao.updateTier(tier.toEntity())

        override fun getAllTiers(): Flow<List<ContactTierDto>> =
            contactTierDao.getAllTiers().transform { tiers ->
                tiers.map { it.toModel() }
            }

        override fun getTierById(id: Long): Flow<ContactTierDto?> =
            contactTierDao.getTierById(id).transform {
                it?.toModel()
            }

        override suspend fun deleteAllTiers() = contactTierDao.deleteAllTiers()

        // Communication Logs operations
        override suspend fun insertCommunicationLog(log: CommunicationLogDto) = communicationLogDao.insertCommunicationLog(log.toEntity())

        override fun getCommunicationLogsForContact(contactId: Long): Flow<List<CommunicationLogDto>> =
            communicationLogDao.getCommunicationLogsForContact(contactId).transform { logs ->
                logs.map { it.toModel() }
            }

        override suspend fun updateCommunicationLog(log: CommunicationLogDto) =
            communicationLogDao.updateCommunicationLog(
                log.toEntity(),
            )

        override suspend fun deleteCommunicationLog(log: CommunicationLogDto) =
            communicationLogDao.deleteCommunicationLog(
                log.toEntity(),
            )

        override suspend fun deleteCommunicationLogsForContact(contactId: Long) =
            communicationLogDao.deleteCommunicationLogsForContact(contactId)

        override suspend fun deleteAllCommunicationLogs() = communicationLogDao.deleteAllCommunicationLogs()

        override fun getAllCommunicationLogs(): Flow<List<CommunicationLogDto>> =
            communicationLogDao.getAllCommunicationLogs().transform { logs ->
                logs.map { it.toModel() }
            }

        override fun getCommunicationLogsByDate(date: LocalDate): Flow<List<CommunicationLogDto>> =
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
                contactDao.getAllContactsAtOnce().map { it.toModel() }
            }
        }
    }
