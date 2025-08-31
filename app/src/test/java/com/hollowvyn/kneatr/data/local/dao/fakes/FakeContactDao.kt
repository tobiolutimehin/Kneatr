package com.hollowvyn.kneatr.data.local.dao.fakes

import com.hollowvyn.kneatr.data.local.dao.ContactDao
import com.hollowvyn.kneatr.data.local.entity.CommunicationLogEntity
import com.hollowvyn.kneatr.data.local.entity.ContactEntity
import com.hollowvyn.kneatr.data.local.entity.ContactTagEntity
import com.hollowvyn.kneatr.data.local.entity.ContactTierEntity
import com.hollowvyn.kneatr.data.local.entity.relation.ContactWithDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeContactDao : ContactDao {
    private val contacts = mutableListOf<ContactEntity>()
    private val contactsFlow = MutableStateFlow<List<ContactWithDetails>>(emptyList())

    // We need to simulate ContactWithDetails relations for testing, so allow setting relations externally or keep empty for now
    private val tagsByContact = mutableMapOf<Long, List<ContactTagEntity>>()
    private val tiersById = mutableMapOf<Long, ContactTierEntity>()
    private val commLogsByContact = mutableMapOf<Long, List<CommunicationLogEntity>>()

    private fun updateFlow() {
        val contactDetailsList =
            contacts
                .map { contact ->
                    ContactWithDetails(
                        contact = contact,
                        tags = tagsByContact[contact.contactId] ?: emptyList(),
                        tier = tiersById[contact.tierId ?: -1],
                        communicationLogs = commLogsByContact[contact.contactId] ?: emptyList(),
                    )
                }.sortedBy { it.contact.name }
        contactsFlow.value = contactDetailsList
    }

    override suspend fun insertContacts(contacts: List<ContactEntity>) {
        contacts.forEach { contact ->
            this.contacts.removeAll { it.contactId == contact.contactId }
            this.contacts.add(contact)
        }
        updateFlow()
    }

    override suspend fun insertContact(contact: ContactEntity) {
        contacts.removeAll { it.contactId == contact.contactId }
        contacts.add(contact)
        updateFlow()
    }

    override suspend fun updateContact(contact: ContactEntity) {
        val idx = contacts.indexOfFirst { it.contactId == contact.contactId }
        if (idx != -1) {
            contacts[idx] = contact
            updateFlow()
        }
    }

    override fun getAllContacts(): Flow<List<ContactWithDetails>> = contactsFlow

    override suspend fun getAllContactsAtOnce(): List<ContactWithDetails> = emptyList()

    override fun getContactsByTierId(tierId: Long): Flow<List<ContactWithDetails>> =
        contactsFlow.map { list -> list.filter { it.contact.tierId == tierId } }

    override fun searchContactsByNamePhoneOrEmail(query: String): Flow<List<ContactWithDetails>> =
        contactsFlow.map { list ->
            list.filter {
                it.contact.name.contains(query, ignoreCase = true) ||
                    it.contact.phoneNumber.contains(query) ||
                    (it.contact.email?.contains(query, ignoreCase = true) ?: false)
            }
        }

    override fun getContactById(id: Long): Flow<ContactWithDetails?> = contactsFlow.map { list -> list.find { it.contact.contactId == id } }

    override fun getContactsByTagId(tagId: Long): Flow<List<ContactWithDetails>> =
        contactsFlow.map { list ->
            list.filter { contactWithDetails ->
                (
                    tagsByContact[contactWithDetails.contact.contactId]?.any { it.tagId == tagId }
                        ?: false
                )
            }
        }

    override suspend fun deleteContactById(id: Long) {
        return
    }

    // Utility functions for test setup:
    fun setTagsForContact(
        contactId: Long,
        tags: List<ContactTagEntity>,
    ) {
        tagsByContact[contactId] = tags
        updateFlow()
    }

    fun setTier(tier: ContactTierEntity) {
        tiersById[tier.tierId] = tier
        updateFlow()
    }

    fun setCommunicationLogs(
        contactId: Long,
        logs: List<CommunicationLogEntity>,
    ) {
        commLogsByContact[contactId] = logs
        updateFlow()
    }
}
