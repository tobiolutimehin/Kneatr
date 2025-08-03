package com.hollowvyn.kneatr.data.remote

import android.content.ContentResolver
import android.provider.ContactsContract
import com.hollowvyn.kneatr.di.IoDispatcher
import com.hollowvyn.kneatr.domain.model.ContactDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactFetcherImpl
    @Inject
    constructor(
        private val contentResolver: ContentResolver,
        @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : ContactFetcher {
        private val cacheMutex = Mutex()
        private var cache: Set<ContactDto>? = null

        // TODO: Update ContactDto and fetcher to support multiple phone numbers and emails per contact.
        override suspend fun fetchContacts(): List<ContactDto> =
            cacheMutex.withLock {
                cache?.let { return it.toList() }

            return withContext(dispatcher) {
                val contacts = mutableListOf<ContactDto>()
                val projection =
                    arrayOf(ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME)
                val cursor =
                    contentResolver.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        projection,
                        null,
                        null,
                        "${ContactsContract.Contacts.DISPLAY_NAME} ASC",
                    )
                cursor?.use { c ->
                    val idIndex = c.getColumnIndexOrThrow(ContactsContract.Contacts._ID)
                    val nameIndex = c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)

                    while (c.moveToNext()) {
                        val id = c.getLong(idIndex)
                        val name = c.getString(nameIndex) ?: "Unknown"

                        val phone = fetchPhoneNumbers(id).firstOrNull() ?: ""
                        val email = fetchEmails(id).firstOrNull()

                        contacts.add(ContactDto(id, name, phone, email))
                    }
                }
                cache = contacts.toSet()
                contacts
            }
        }

    private fun fetchPhoneNumbers(contactId: Long): List<String> {
        val phones = mutableListOf<String>()
        val cursor =
            contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                arrayOf(contactId.toString()),
                null,
            )
        cursor?.use {
            val index = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (it.moveToNext()) {
                phones.add(it.getString(index))
            }
        }
        return phones
    }

    private fun fetchEmails(contactId: Long): List<String> {
        val emails = mutableListOf<String>()
        val cursor =
            contentResolver.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                arrayOf(ContactsContract.CommonDataKinds.Email.ADDRESS),
                "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?",
                arrayOf(contactId.toString()),
                null,
            )
        cursor?.use {
            val index = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.ADDRESS)
            while (it.moveToNext()) {
                emails.add(it.getString(index))
            }
        }
        return emails
    }

    suspend fun clearCache() {
        cacheMutex.withLock {
            cache = null
        }
    }
}
