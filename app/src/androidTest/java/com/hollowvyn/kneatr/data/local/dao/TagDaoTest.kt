package com.hollowvyn.kneatr.data.local.dao

import com.hollowvyn.kneatr.data.local.entity.ContactEntity
import com.hollowvyn.kneatr.data.local.entity.TagEntity
import com.hollowvyn.kneatr.data.local.entity.crossRef.ContactTagCrossRef
import com.hollowvyn.kneatr.di.DatabaseModule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
class TagDaoTest : BaseDaoTest() {
    private lateinit var tagDao: TagDao
    private lateinit var contactDao: ContactDao

    @Before
    fun setupDao() {
        tagDao = database.tagDao()
        contactDao = database.contactDao()
    }

    @Test
    fun insert_and_query_tag_by_id_and_name() =
        runTest {
            val tag = TagEntity(tagId = 1, name = "Friends")
            tagDao.insertTag(tag)

            val retrievedById = tagDao.getTagById(1).first()
            val retrievedByName = tagDao.getTagByName("Friends").first()

            assertNotNull(retrievedById)
            assertEquals("Friends", retrievedById?.name)
            assertNotNull(retrievedByName)
            assertEquals(1, retrievedByName?.tagId)
        }

    @Test
    fun get_all_tags() =
        runTest {
            val tag1 = TagEntity(tagId = 1, name = "Friends")
            val tag2 = TagEntity(tagId = 2, name = "Work")
            tagDao.insertTag(tag1)
            tagDao.insertTag(tag2)

            val tags = tagDao.getAllTags().first()
            assertEquals(2, tags.size)
        }

    @Test
    fun update_tag_successfully() =
        runTest {
            val tag = TagEntity(tagId = 1, name = "Friends")
            tagDao.insertTag(tag)

            val updated = tag.copy(name = "Close Friends")
            tagDao.updateTag(updated)

            val retrieved = tagDao.getTagById(1).first()
            assertEquals("Close Friends", retrieved?.name)
        }

    @Test
    fun delete_tag_successfully() =
        runTest {
            val tag = TagEntity(tagId = 1, name = "Friends")
            tagDao.insertTag(tag)
            tagDao.deleteTag(tag)

            val tags = tagDao.getAllTags().first()
            assertTrue(tags.isEmpty())
        }

    @Test
    fun get_tags_with_contacts() =
        runTest {
            val tag = TagEntity(tagId = 1, name = "Friends")
            tagDao.insertTag(tag)

            val contact =
                ContactEntity(
                    contactId = 1,
                    name = "Ada Lovelace",
                    phoneNumber = "1234567890",
                )
            contactDao.insertContact(contact)

            database.contactTagCrossRefDao().addTagToContact(
                ContactTagCrossRef(contactId = 1, tagId = 1),
            )

            val tagsWithContacts = tagDao.getTagsWithContacts().first()
            assertEquals(1, tagsWithContacts.size)
            assertEquals("Friends", tagsWithContacts.first().tag.name)
            assertEquals(1, tagsWithContacts.first().contacts.size)
            assertEquals(
                "Ada Lovelace",
                tagsWithContacts
                    .first()
                    .contacts
                    .first()
                    .name,
            )
        }

    @Test
    fun get_tag_with_contacts_by_id_and_name() =
        runTest {
            val tag = TagEntity(tagId = 1, name = "Friends")
            tagDao.insertTag(tag)

            val contact =
                ContactEntity(
                    contactId = 1,
                    name = "Ada Lovelace",
                    phoneNumber = "1234567890",
                )
            contactDao.insertContact(contact)

            database.contactTagCrossRefDao().addTagToContact(
                ContactTagCrossRef(contactId = 1, tagId = 1),
            )

            val resultById = tagDao.getTagWithContactsById(1).first()
            assertNotNull(resultById)
            assertEquals("Friends", resultById?.tag?.name)
            assertEquals(1, resultById?.contacts?.size)

            val resultByName = tagDao.getTagWithContactsByName("Friends").first()
            assertNotNull(resultByName)
            assertEquals("Friends", resultByName?.tag?.name)
            assertEquals(1, resultByName?.contacts?.size)
        }
}
