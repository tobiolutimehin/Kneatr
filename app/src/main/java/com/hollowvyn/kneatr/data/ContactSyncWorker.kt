package com.hollowvyn.kneatr.data

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.hollowvyn.kneatr.data.repository.ContactsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ContactSyncWorker
    @AssistedInject
    constructor(
        @Assisted appContext: Context,
        @Assisted workerParams: WorkerParameters,
        private val contactsRepository: ContactsRepository,
    ) : CoroutineWorker(appContext, workerParams) {
        override suspend fun doWork(): Result =
            try {
                contactsRepository.syncContacts()
                Result.success()
            } catch (e: Exception) {
                Log.e(ContactSyncWorker::class.simpleName, e.message ?: "Unknown error")
                Result.retry()
            }
    }

// This tag will be applied to BOTH periodic and one-time sync workers.
const val CONTACT_SYNC_TAG = "ContactSyncTag"

// This unique name is ONLY for the periodic worker, to prevent scheduling it multiple times.
const val PERIODIC_CONTACT_SYNC_WORKER_NAME = "PeriodicContactSyncWork"

// This unique name is ONLY for the one-time manual refresh.
const val MANUAL_CONTACT_REFRESH_WORKER_NAME = "ManualContactRefresh"
