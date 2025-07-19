package com.hollowvyn.kneatr.di

import android.content.Context
import androidx.room.Room
import com.hollowvyn.kneatr.data.local.dao.CommunicationLogDao
import com.hollowvyn.kneatr.data.local.dao.ContactDao
import com.hollowvyn.kneatr.data.local.dao.ContactTagCrossRefDao
import com.hollowvyn.kneatr.data.local.dao.ContactTagDao
import com.hollowvyn.kneatr.data.local.dao.ContactTierDao
import com.hollowvyn.kneatr.data.local.database.KneatrDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): KneatrDatabase =
        Room
            .databaseBuilder(
                context,
                KneatrDatabase::class.java,
                name = "kneatr_database",
            ).fallbackToDestructiveMigration(false)
            .build()

    @Provides
    fun provideContactTagDao(database: KneatrDatabase): ContactTagDao = database.tagDao()

    @Provides
    fun provideContactTagCrossRefDao(database: KneatrDatabase): ContactTagCrossRefDao = database.contactTagCrossRefDao()

    @Provides
    fun provideContactTierDao(database: KneatrDatabase): ContactTierDao = database.contactTierDao()

    @Provides
    fun provideContactDao(database: KneatrDatabase): ContactDao = database.contactDao()

    @Provides
    fun provideCommunicationLogDao(database: KneatrDatabase): CommunicationLogDao = database.communicationLogDao()
}
