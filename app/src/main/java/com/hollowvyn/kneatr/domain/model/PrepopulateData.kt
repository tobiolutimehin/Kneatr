package com.hollowvyn.kneatr.domain.model

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hollowvyn.kneatr.data.local.database.KneatrDatabase
import com.hollowvyn.kneatr.data.local.entity.ContactTierEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class PrepopulateData
    @Inject
    constructor(
        private val databaseProvider: Provider<KneatrDatabase>,
    ) : RoomDatabase.Callback() {
        private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            applicationScope.launch {
                populateTiers()
            }
        }

        private suspend fun populateTiers() {
            val defaultTiers =
                listOf(
                    ContactTierEntity(tierId = 1, name = "Tier 1", daysBetweenContact = 7),
                    ContactTierEntity(tierId = 2, name = "Tier 2", daysBetweenContact = 14),
                    ContactTierEntity(tierId = 3, name = "Tier 3", daysBetweenContact = 30),
                    ContactTierEntity(tierId = 4, name = "Tier 4", daysBetweenContact = 90),
                    ContactTierEntity(tierId = 5, name = "Tier 5", daysBetweenContact = 180),
                )
            databaseProvider.get().contactTierDao().insertTiers(defaultTiers)
        }
    }
