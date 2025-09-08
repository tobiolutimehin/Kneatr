package com.hollowvyn.kneatr

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
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
            PeriodicWorkRequestBuilder<ContactSyncWorker>(14, TimeUnit.DAYS)
                .setConstraints(
                    Constraints
                        .Builder()
                        .setRequiresBatteryNotLow(true)
                        .build(),
                ).build()

        WorkManager.getInstance(context.applicationContext).enqueueUniquePeriodicWork(
            "ContactSyncWork",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest,
        )
    }

    fun triggerImmediateContactSync(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w("UserRefresh", "READ_CONTACTS permission not granted. Cannot sync.")
            return
        }

        val constraints =
            Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val immediateRefreshWorkRequest =
            OneTimeWorkRequestBuilder<ContactSyncWorker>()
                .setConstraints(constraints)
                .addTag("ImmediateContactSync")
                .build()

        WorkManager.getInstance(context.applicationContext).enqueueUniqueWork(
            "ManualContactRefresh",
            ExistingWorkPolicy.KEEP,
            immediateRefreshWorkRequest,
        )
        Log.d("UserRefresh", "Immediate contact sync requested by user.")
    }
}
