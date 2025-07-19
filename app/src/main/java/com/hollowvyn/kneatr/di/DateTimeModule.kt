package com.hollowvyn.kneatr.di

import com.hollowvyn.kneatr.data.util.DateTimeHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.TimeZone
import javax.inject.Singleton
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Module
@InstallIn(SingletonComponent::class)
object DateTimeModule {
    @Provides
    @Singleton
    fun provideClock(): Clock = Clock.System

    @Provides
    @Singleton
    fun provideTimeZone(): TimeZone = TimeZone.currentSystemDefault()

    @Provides
    @Singleton
    fun provideDateTimeHelper(
        clock: Clock,
        timeZone: TimeZone,
    ): DateTimeHelper = DateTimeHelper(clock, timeZone)
}
