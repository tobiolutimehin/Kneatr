package com.hollowvyn.kneatr

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
import com.hollowvyn.kneatr.ui.contact.ContactsListScreen
import com.hollowvyn.kneatr.ui.theme.KneatrTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            KneatrTheme {
                RequestContactsPermissionEffect()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ContactsListScreen(modifier = Modifier.padding(innerPadding))
                }
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
                    Log.d("ExampleScreen", "PERMISSION GRANTED")
                    application.schedulePeriodicContactSync(context)
                } else {
                    Log.d("ExampleScreen", "PERMISSION DENIED")
                }
            }

        LaunchedEffect(Unit) {
            if (PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_CONTACTS,
                )
            ) {
                Log.d("ExampleScreen", "PERMISSION ALREADY GRANTED")
            } else {
                Log.d("ExampleScreen", "REQUESTING PERMISSION")
                launcher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }
}
