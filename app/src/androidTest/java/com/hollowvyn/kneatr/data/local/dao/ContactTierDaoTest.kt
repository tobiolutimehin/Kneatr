package com.hollowvyn.kneatr.data.local.dao

import com.hollowvyn.kneatr.data.local.entity.ContactEntity
import com.hollowvyn.kneatr.data.local.entity.ContactTierEntity
import com.hollowvyn.kneatr.di.DatabaseModule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
class ContactTierDaoTest : BaseDaoTest() {
    private lateinit var tierDao: ContactTierDao
    private lateinit var contactDao: ContactDao

    @Before
    fun setupDao() {
        tierDao = database.contactTierDao()
        contactDao = database.contactDao()
    }

    @Test
    fun insert_and_get_all_tiers() =
        runTest {
            val tiers =
                listOf(
                    ContactTierEntity(1, "Tier 1", 7),
                    ContactTierEntity(2, "Tier 2", 14),
                )
            tierDao.insertTiers(tiers)

            val result = tierDao.getAllTiers()
            assertEquals(2, result.size)
            assertTrue(result.any { it.name == "Tier 1" })
            assertTrue(result.any { it.name == "Tier 2" })
        }

    @Test
    fun insert_single_and_query_by_id() =
        runTest {
            val tier = ContactTierEntity(1, "Close Friends", 7)
            tierDao.insertTier(tier)

            val result = tierDao.getTierById(1)
            assertNotNull(result)
            assertEquals("Close Friends", result?.name)
        }

    @Test
    fun update_tier() =
        runTest {
            val tier = ContactTierEntity(1, "Close Friends", 7)
            tierDao.insertTier(tier)

            val updated = tier.copy(name = "Very Close Friends", daysBetweenContact = 3)
            tierDao.updateTier(updated)

            val result = tierDao.getTierById(1)
            assertEquals("Very Close Friends", result?.name)
            assertEquals(3, result?.daysBetweenContact)
        }

    @Test
    fun delete_all_tiers() =
        runTest {
            val tier = ContactTierEntity(1, "Close Friends", 7)
            tierDao.insertTier(tier)

            tierDao.deleteAllTiers()

            val result = tierDao.getAllTiers()
            assertTrue(result.isEmpty())
        }

    @Test
    fun get_tiers_with_contacts() =
        runTest {
            val tier = ContactTierEntity(1, "Close Friends", 7)
            tierDao.insertTier(tier)

            val contact =
                ContactEntity(
                    contactId = 1,
                    name = "Ada Lovelace",
                    phoneNumber = "123",
                    tierId = 1,
                )
            contactDao.insertContact(contact)

            val result = tierDao.getTiersWithContacts()
            assertEquals(1, result.size)
            assertEquals(1, result.first().contacts.size)
            assertEquals(
                "Ada Lovelace",
                result
                    .first()
                    .contacts
                    .first()
                    .name,
            )
        }

    @Test
    fun get_tier_with_contacts_by_id() =
        runTest {
            val tier = ContactTierEntity(1, "Close Friends", 7)
            tierDao.insertTier(tier)

            val contact =
                ContactEntity(
                    contactId = 1,
                    name = "Ada Lovelace",
                    phoneNumber = "123",
                    tierId = 1,
                )
            contactDao.insertContact(contact)

            val result = tierDao.getTierWithContactsById(1)
            assertNotNull(result)
            assertEquals(1, result?.contacts?.size)
            assertEquals("Ada Lovelace", result?.contacts?.first()?.name)
        }

    @Test
    fun get_tier_with_contacts_by_name() =
        runTest {
            val tier = ContactTierEntity(1, "Close Friends", 7)
            tierDao.insertTier(tier)

            val contact =
                ContactEntity(
                    contactId = 1,
                    name = "Ada Lovelace",
                    phoneNumber = "123",
                    tierId = 1,
                )
            contactDao.insertContact(contact)

            val result = tierDao.getTierWithContactsByName("Close Friends")
            assertNotNull(result)
            assertEquals(1, result?.contacts?.size)
            assertEquals("Ada Lovelace", result?.contacts?.first()?.name)
        }
}
