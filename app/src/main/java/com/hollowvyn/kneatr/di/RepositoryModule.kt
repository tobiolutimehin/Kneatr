package com.hollowvyn.kneatr.di

import com.hollowvyn.kneatr.data.repository.ContactsRepository
import com.hollowvyn.kneatr.data.repository.ContactsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindsContactsRepository(contactsRepository: ContactsRepositoryImpl): ContactsRepository
}
