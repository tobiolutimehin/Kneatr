package com.hollowvyn.kneatr

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.hollowvyn.kneatr.data.ContactSyncWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class KneatrApplication :
    Application(),
    Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

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
