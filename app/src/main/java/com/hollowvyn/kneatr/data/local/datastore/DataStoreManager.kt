package com.hollowvyn.kneatr.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

@Singleton
class DataStoreManager
    @Inject
    constructor(
        @param:ApplicationContext private val context: Context,
    ) {
        private object PreferencesKeys {
            val IS_FIRST_RUN = booleanPreferencesKey(IS_FIRST_RUN_TAG)
        }

        val isFirstRunFlow: Flow<Boolean> =
            context.dataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }.map { preferences ->
                    preferences[PreferencesKeys.IS_FIRST_RUN] ?: true
                }

        suspend fun setIsFirstRun(isFirstRun: Boolean) {
            context.dataStore.edit { settings ->
                settings[PreferencesKeys.IS_FIRST_RUN] = isFirstRun
            }
        }

        companion object {
            private const val IS_FIRST_RUN_TAG = "is_first_run"
        }
    }
