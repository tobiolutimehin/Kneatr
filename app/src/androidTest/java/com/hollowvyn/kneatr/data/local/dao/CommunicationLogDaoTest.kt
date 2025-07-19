package com.hollowvyn.kneatr.data.local.dao

import com.hollowvyn.kneatr.data.local.entity.CommunicationLogEntity
import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import com.hollowvyn.kneatr.data.local.entity.ContactEntity
import com.hollowvyn.kneatr.di.DatabaseModule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
class CommunicationLogDaoTest : BaseDaoTest() {
    private lateinit var communicationLogDao: CommunicationLogDao
    private lateinit var contactDao: ContactDao

    @Before
    fun setup() {
        communicationLogDao = database.communicationLogDao()
        contactDao = database.contactDao()
    }

    private val sampleContact =
        ContactEntity(
            contactId = 1,
            name = "Test Contact",
            phoneNumber = "1234567890",
            email = "test@example.com",
        )

    private val sampleLog1 =
        CommunicationLogEntity(
            communicationId = 0,
            type = CommunicationType.PHONE_CALL,
            date = LocalDate(2024, 1, 1),
            contactId = 1,
            notes = "First call",
        )

    private val sampleLog2 =
        CommunicationLogEntity(
            communicationId = 0,
            type = CommunicationType.EMAIL,
            date = LocalDate(2024, 1, 2),
            contactId = 1,
            notes = "Follow-up email",
        )

    @Test
    fun insert_and_getLogsByContactId() =
        runTest {
            contactDao.insertContact(sampleContact)
            communicationLogDao.insertCommunicationLog(sampleLog1)
            communicationLogDao.insertCommunicationLog(sampleLog2)

            val logs = communicationLogDao.getCommunicationLogsForContact(1).first()
            assertEquals(2, logs.size)
            assertTrue(logs.any { it.notes == "First call" })
            assertTrue(logs.any { it.type == CommunicationType.EMAIL })
        }

    @Test
    fun update_communicationLog() =
        runTest {
            contactDao.insertContact(sampleContact)
            communicationLogDao.insertCommunicationLog(sampleLog1)

            val insertedLog = communicationLogDao.getCommunicationLogsForContact(1).first().first()
            val updatedLog = insertedLog.copy(notes = "Updated notes")
            communicationLogDao.updateCommunicationLog(updatedLog)

            val logsAfterUpdate = communicationLogDao.getCommunicationLogsForContact(1).first()
            assertEquals("Updated notes", logsAfterUpdate.first().notes)
        }

    @Test
    fun delete_communicationLog() =
        runTest {
            contactDao.insertContact(sampleContact)
            communicationLogDao.insertCommunicationLog(sampleLog1)

            val insertedLog = communicationLogDao.getCommunicationLogsForContact(1).first().first()
            communicationLogDao.deleteCommunicationLog(insertedLog)

            val logs = communicationLogDao.getCommunicationLogsForContact(1).first()
            assertTrue(logs.isEmpty())
        }

    @Test
    fun deleteAllLogsForContact() =
        runTest {
            contactDao.insertContact(sampleContact)
            communicationLogDao.insertCommunicationLog(sampleLog1)
            communicationLogDao.insertCommunicationLog(sampleLog2)

            communicationLogDao.deleteCommunicationLogsForContact(1)

            val logs = communicationLogDao.getCommunicationLogsForContact(1).first()
            assertTrue(logs.isEmpty())
        }

    @Test
    fun deleteAllLogs() =
        runTest {
            contactDao.insertContact(sampleContact)
            communicationLogDao.insertCommunicationLog(sampleLog1)
            communicationLogDao.insertCommunicationLog(sampleLog2)

            communicationLogDao.deleteAllCommunicationLogs()

            val logs = communicationLogDao.getAllCommunicationLogs().first()
            assertTrue(logs.isEmpty())
        }

    @Test
    fun getLogsByDate() =
        runTest {
            contactDao.insertContact(sampleContact)
            communicationLogDao.insertCommunicationLog(sampleLog1)
            communicationLogDao.insertCommunicationLog(sampleLog2)

            val logsOnJan1 =
                communicationLogDao.getCommunicationLogsByDate(LocalDate(2024, 1, 1)).first()
            assertEquals(1, logsOnJan1.size)
            assertEquals(CommunicationType.PHONE_CALL, logsOnJan1.first().type)
        }
}
