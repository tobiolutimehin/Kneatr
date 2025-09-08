package com.hollowvyn.kneatr

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import com.hollowvyn.kneatr.ui.contact.ContactsListScreen
import com.hollowvyn.kneatr.ui.theme.KneatrTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var prefsManager: PrefsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            KneatrTheme {
                RequestContactsPermissionEffect()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ContactsListScreen(modifier = Modifier.padding(innerPadding))
                }
//                GoToSettingsPermissionScreen()
            }
        }
    }

    @Composable
    private fun RequestContactsPermissionEffect() {
        val context = LocalContext.current
        val application = context.applicationContext as KneatrApplication

        val launcher =
            rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission(),
            ) { isGranted: Boolean ->
                if (isGranted) {
                    application.triggerImmediateContactSync(context)
                }
            }

        LaunchedEffect(Unit) {
            if (PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_CONTACTS,
                )
            ) {
                if (prefsManager.isFirstLaunch) {
                    application.triggerImmediateContactSync(context)
                    prefsManager.isFirstLaunch = false // Mark it as done.
                }
            } else {
                launcher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }
}

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
