package com.hollowvyn.kneatr.data.cache

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefsManager
    @Inject
    constructor(
        @ApplicationContext context: Context,
    ) {
        private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        companion object {
            private const val KEY_IS_FIRST_RUN = "is_first_run"
        }

        var isFirstLaunch: Boolean
            get() = prefs.getBoolean(KEY_IS_FIRST_RUN, true)
            set(value) = prefs.edit { putBoolean(KEY_IS_FIRST_RUN, value) }
    }
