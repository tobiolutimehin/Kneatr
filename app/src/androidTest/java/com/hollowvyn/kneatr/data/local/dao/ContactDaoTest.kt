package com.hollowvyn.kneatr.data.local.dao

import com.hollowvyn.kneatr.data.local.entity.ContactEntity
import com.hollowvyn.kneatr.data.local.entity.ContactTierEntity
import com.hollowvyn.kneatr.di.DatabaseModule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
class ContactDaoTest : BaseDaoTest() {
    private lateinit var contactDao: ContactDao
    private lateinit var tierDao: ContactTierDao

    @Before
    fun setupDao() {
        contactDao = database.contactDao()
        tierDao = database.contactTierDao()
    }

    @Test
    fun insert_and_query_contacts() =
        runTest {
            val contact1 =
                ContactEntity(
                    contactId = 1,
                    name = "Ada Lovelace",
                    phoneNumber = "1234567890",
                )
            val contact2 =
                ContactEntity(
                    contactId = 2,
                    name = "Alan Turing",
                    phoneNumber = "0987654321",
                )

            contactDao.insertContacts(listOf(contact1, contact2))
            val contacts = contactDao.getAllContacts().first()

            assertEquals(2, contacts.size)
            assertTrue(contacts.any { it.contact.name == "Ada Lovelace" })
            assertTrue(contacts.any { it.contact.name == "Alan Turing" })
        }

    @Test
    fun update_contact_updates_successfully() =
        runTest {
            val contact =
                ContactEntity(
                    contactId = 1,
                    name = "Ada Lovelace",
                    phoneNumber = "1234567890",
                )
            contactDao.insertContact(contact)

            val updated = contact.copy(email = "ada@history.com")
            contactDao.updateContact(updated)

            val result = contactDao.getContactById(1).first()
            assertEquals("ada@history.com", result?.contact?.email)
        }

    @Test
    fun query_contacts_by_tier() =
        runTest {
            val tier =
                ContactTierEntity(
                    tierId = 1,
                    name = "Close Friends",
                    daysBetweenContact = 7,
                )
            tierDao.insertTier(tier)

            val contact =
                ContactEntity(
                    contactId = 1,
                    name = "Ada Lovelace",
                    phoneNumber = "1234567890",
                    tierId = tier.tierId,
                )
            contactDao.insertContact(contact)

            val result = contactDao.getContactsByTierId(1).first()
            assertEquals(1, result.size)
            assertEquals("Ada Lovelace", result.first().contact.name)
        }

    @Test
    fun query_contacts_by_search() =
        runTest {
            val contact =
                ContactEntity(
                    contactId = 1,
                    name = "Ada Lovelace",
                    phoneNumber = "1234567890",
                    email = "ada@history.com",
                )
            contactDao.insertContact(contact)

            val result = contactDao.searchContactsByNamePhoneOrEmail("Ada").first()
            assertEquals(1, result.size)
            assertEquals("Ada Lovelace", result.first().contact.name)

            val resultByPhone = contactDao.searchContactsByNamePhoneOrEmail("1234").first()
            assertEquals(1, resultByPhone.size)

            val resultByEmail = contactDao.searchContactsByNamePhoneOrEmail("history").first()
            assertEquals(1, resultByEmail.size)
        }

    @OptIn(ExperimentalTime::class)
    @Test
    fun query_overdue_contacts() =
        runTest {
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

            val contact1 =
                ContactEntity(
                    contactId = 1,
                    name = "Ada Lovelace",
                    phoneNumber = "1234567890",
                    nextContactDate = today.minus(2, DateTimeUnit.DAY),
                )
            val contact2 =
                ContactEntity(
                    contactId = 2,
                    name = "Alan Turing",
                    phoneNumber = "0987654321",
                    nextContactDate = today.plus(10, DateTimeUnit.DAY),
                )
            contactDao.insertContacts(listOf(contact1, contact2))

            val result = contactDao.getOverdueContacts(today).first()
            assertEquals(1, result.size)
            assertEquals("Ada Lovelace", result.first().contact.name)
        }

    @Test
    fun get_contact_by_id_returns_correct_contact() =
        runTest {
            val contact =
                ContactEntity(
                    contactId = 1,
                    name = "Ada Lovelace",
                    phoneNumber = "1234567890",
                )
            contactDao.insertContact(contact)

            val result = contactDao.getContactById(1).first()

            assertNotNull(result)
            assertEquals("Ada Lovelace", result?.contact?.name)
            assertEquals("1234567890", result?.contact?.phoneNumber)
        }
}
