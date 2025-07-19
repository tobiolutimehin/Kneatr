package com.hollowvyn.kneatr.data.local.dao.fakes

import com.hollowvyn.kneatr.data.local.dao.ContactTagDao
import com.hollowvyn.kneatr.data.local.entity.ContactTagEntity
import com.hollowvyn.kneatr.data.local.entity.relation.ContactWithDetails
import com.hollowvyn.kneatr.data.local.entity.relation.TagWithContacts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeContactTagDao : ContactTagDao {
    private val tags = mutableListOf<ContactTagEntity>()
    private val tagsFlow = MutableStateFlow<List<ContactTagEntity>>(emptyList())

    // Map tagId to contacts for TagWithContacts relation
    private val contactsByTagId = mutableMapOf<Long, List<ContactWithDetails>>()

    private fun updateFlow() {
        tagsFlow.value = tags.toList()
    }

    override fun getAllTags(): Flow<List<ContactTagEntity>> = tagsFlow

    override fun getTagsWithContacts(): Flow<List<TagWithContacts>> =
        tagsFlow.map { currentTags ->
            currentTags.map { tag ->
                TagWithContacts(
                    tag = tag,
                    contacts = emptyList(),
                )
            }
        }

    override fun getTagWithContactsById(id: Int): Flow<TagWithContacts?> =
        getTagsWithContacts().map { list -> list.find { it.tag.tagId.toInt() == id } }

    override fun getTagWithContactsByName(name: String): Flow<TagWithContacts?> =
        getTagsWithContacts().map { list -> list.find { it.tag.name == name } }

    override fun getTagById(id: Int): Flow<ContactTagEntity?> = tagsFlow.map { list -> list.find { it.tagId.toInt() == id } }

    override fun getTagByName(name: String): Flow<ContactTagEntity?> = tagsFlow.map { list -> list.find { it.name == name } }

    override suspend fun insertTag(tag: ContactTagEntity) {
        if (tags.none { it.tagId == tag.tagId }) {
            tags.add(tag)
            updateFlow()
        }
    }

    override suspend fun updateTag(tag: ContactTagEntity) {
        val idx = tags.indexOfFirst { it.tagId == tag.tagId }
        if (idx != -1) {
            tags[idx] = tag
            updateFlow()
        }
    }

    override suspend fun deleteTag(tag: ContactTagEntity) {
        tags.removeIf { it.tagId == tag.tagId }
        updateFlow()
    }

    // Utility:
    fun setContactsForTag(
        tagId: Long,
        contacts: List<ContactWithDetails>,
    ) {
        contactsByTagId[tagId] = contacts
    }
}
