package com.hollowvyn.kneatr

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.hollowvyn.kneatr.data.ContactSyncWorker
import com.hollowvyn.kneatr.data.repository.ContactsRepository
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class KneatrApplication :
    Application(),
    Configuration.Provider {
    @Inject
    lateinit var workerFactory: CustomWorkerFactory

    override val workManagerConfiguration: Configuration
        get() =
            Configuration
                .Builder()
                .setWorkerFactory(workerFactory)
                .build()

    override fun onCreate() {
        super.onCreate()
        schedulePeriodicContactSync(this)
    }

    fun schedulePeriodicContactSync(context: Context) {
        val workRequest =
            PeriodicWorkRequestBuilder<ContactSyncWorker>(7, TimeUnit.DAYS)
                .setConstraints(
                    Constraints
                        .Builder()
                        .build(),
                ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "ContactSyncWork",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest,
        )
    }
}

class CustomWorkerFactory
    @Inject
    constructor(
        private val repository: ContactsRepository,
    ) : WorkerFactory() {
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters,
        ): ListenableWorker? =
            ContactSyncWorker(
                appContext,
                workerParameters,
            repository,
        )
}
