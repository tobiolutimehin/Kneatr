package com.hollowvyn.kneatr

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PermContactCalendar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hollowvyn.kneatr.ui.theme.KneatrTheme

/**
 * A full-screen composable shown when the app's core permission (READ_CONTACTS) is denied.
 * It provides a clear explanation and a button to open the app's settings.
 *
 * This version is designed to be the "main gate" and triggers a permission request
 * via the provided lambda.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param onGrantPermissionClick A lambda that will be invoked to request the permission.
 *                               This is typically `launcher.launch(Manifest.permission.READ_CONTACTS)`.
 */
@Composable
fun PermissionRequiredScreen(
    modifier: Modifier = Modifier,
    onGrantPermissionClick: () -> Unit = {},
    onExitClick: () -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Using a more context-specific icon for contacts permission
        Icon(
            imageVector = Icons.Outlined.PermContactCalendar,
            contentDescription = null, // The text below describes the purpose
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Permission Required",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Kneatr needs access to your contacts to help you manage and organize them. Please grant the permission to continue.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onGrantPermissionClick) {
            Text("Grant Permission")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // This is the new, secondary action
        TextButton(onClick = onExitClick) {
            Text("Maybe Later")
        }
    }
}

/**
 * A variant of the permission screen that directly opens the app's settings.
 * This is useful if the user has selected "Don't ask again", as the standard
 * permission dialog will no longer appear.
 */
@Composable
fun GoToSettingsPermissionScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val intent =
        remember {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
        }

    PermissionRequiredScreen(
        modifier = modifier,
        onGrantPermissionClick = {
            context.startActivity(intent)
        },
        onExitClick = {
            context.findActivity()?.finish()
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun PermissionRequiredScreenPreview() {
    KneatrTheme {
        Surface {
            PermissionRequiredScreen()
        }
    }
}

private fun Context.findActivity(): Activity? =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
