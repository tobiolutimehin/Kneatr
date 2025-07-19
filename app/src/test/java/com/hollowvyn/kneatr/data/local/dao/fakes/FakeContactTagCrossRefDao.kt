package com.hollowvyn.kneatr.data.local.dao.fakes

import com.hollowvyn.kneatr.data.local.dao.ContactTagCrossRefDao
import com.hollowvyn.kneatr.data.local.entity.crossRef.ContactTagCrossRef

class FakeContactTagCrossRefDao : ContactTagCrossRefDao {
    private val crossRefs = mutableListOf<ContactTagCrossRef>()

    override suspend fun addTagToContact(crossRef: ContactTagCrossRef) {
        if (crossRefs.none { it.contactId == crossRef.contactId && it.tagId == crossRef.tagId }) {
            crossRefs.add(crossRef)
        }
    }

    override suspend fun removeTagFromContact(crossRef: ContactTagCrossRef) {
        crossRefs.removeIf { it.contactId == crossRef.contactId && it.tagId == crossRef.tagId }
    }

    override suspend fun removeAllTagsFromContact(contactId: Long) {
        crossRefs.removeIf { it.contactId == contactId }
    }

    // Utility:
    fun getTagsForContact(contactId: Long): List<ContactTagCrossRef> = crossRefs.filter { it.contactId == contactId }
}
