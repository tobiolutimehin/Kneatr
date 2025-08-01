package com.hollowvyn.kneatr.data.local.dao

import com.hollowvyn.kneatr.data.local.entity.ContactEntity
import com.hollowvyn.kneatr.data.local.entity.ContactTagEntity
import com.hollowvyn.kneatr.data.local.entity.crossRef.ContactTagCrossRef
import com.hollowvyn.kneatr.di.DatabaseModule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
class ContactTagCrossRefDaoTest : BaseDaoTest() {
    private lateinit var crossRefDao: ContactTagCrossRefDao
    private lateinit var contactDao: ContactDao
    private lateinit var contactTagDao: ContactTagDao

    @Before
    fun setupDao() {
        crossRefDao = database.contactTagCrossRefDao()
        contactDao = database.contactDao()
        contactTagDao = database.tagDao()
    }

    @Test
    fun addTagToContact_insertsCorrectly() =
        runTest {
            val contact = ContactEntity(contactId = 1, name = "Ada Lovelace", phoneNumber = "123")
            val tag = ContactTagEntity(tagId = 1, name = "Friends")
            contactDao.insertContact(contact)
            contactTagDao.insertTag(tag)

            crossRefDao.addTagToContact(ContactTagCrossRef(contactId = 1, tagId = 1))

            val contactsWithTags = contactDao.getAllContacts().first()
            assertEquals(1, contactsWithTags.first().tags.size)
            assertEquals(
                "Friends",
                contactsWithTags
                    .first()
                    .tags
                    .first()
                    .name,
            )
        }

    @Test
    fun removeTagFromContact_deletesSingleRelation() =
        runTest {
            val contact = ContactEntity(contactId = 1, name = "Ada Lovelace", phoneNumber = "123")
            val tag = ContactTagEntity(tagId = 1, name = "Friends")
            contactDao.insertContact(contact)
            contactTagDao.insertTag(tag)

            val crossRef = ContactTagCrossRef(contactId = 1, tagId = 1)
            crossRefDao.addTagToContact(crossRef)

            crossRefDao.removeTagFromContact(crossRef)

            val contactsWithTags = contactDao.getAllContacts().first()
            assertTrue(contactsWithTags.first().tags.isEmpty())
        }

    @Test
    fun removeAllTagsFromContact_clearsAllRelations() =
        runTest {
            val contact = ContactEntity(contactId = 1, name = "Ada Lovelace", phoneNumber = "123")
            val tag1 = ContactTagEntity(tagId = 1, name = "Friends")
            val tag2 = ContactTagEntity(tagId = 2, name = "Work")
            contactDao.insertContact(contact)
            contactTagDao.insertTag(tag1)
            contactTagDao.insertTag(tag2)

            crossRefDao.addTagToContact(ContactTagCrossRef(contactId = 1, tagId = 1))
            crossRefDao.addTagToContact(ContactTagCrossRef(contactId = 1, tagId = 2))

            crossRefDao.removeAllTagsFromContact(1)

            val contactsWithTags = contactDao.getAllContacts().first()
            assertTrue(contactsWithTags.first().tags.isEmpty())
        }
}
