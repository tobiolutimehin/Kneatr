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
