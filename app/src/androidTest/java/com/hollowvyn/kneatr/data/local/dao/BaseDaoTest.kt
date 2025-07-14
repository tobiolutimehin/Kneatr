package com.hollowvyn.kneatr.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.hollowvyn.kneatr.data.local.database.KneatrDatabase
import com.hollowvyn.kneatr.di.DatabaseModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.After
import org.junit.Before
import org.junit.Rule

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
abstract class BaseDaoTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    lateinit var database: KneatrDatabase

    @Before
    fun setupDb() {
        database =
            Room
                .inMemoryDatabaseBuilder(
                    ApplicationProvider.getApplicationContext(),
                    KneatrDatabase::class.java,
                ).allowMainThreadQueries() // OK for tests
                .build()
    }

    @After
    fun closeDb() {
        database.close()
    }
}
