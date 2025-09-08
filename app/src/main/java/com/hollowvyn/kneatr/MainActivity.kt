package com.hollowvyn.kneatr

import android.Manifest
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
import com.hollowvyn.kneatr.data.local.datastore.DataStoreManager
import com.hollowvyn.kneatr.ui.contact.ContactsListScreen
import com.hollowvyn.kneatr.ui.theme.KneatrTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var dataStoreManager: DataStoreManager

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
                val isFirstRun = dataStoreManager.isFirstRunFlow.first()
                if (isFirstRun) {
                    application.triggerImmediateContactSync(context)
                    dataStoreManager.setIsFirstRun(true)
                }
            } else {
                launcher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }
}
