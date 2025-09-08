package com.hollowvyn.kneatr.ui.components.screenstates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.VolumeOff,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
        )
        Text(
            text = "Error",
            style = MaterialTheme.typography.headlineLarge,
        )
        Button(
            onClick = { /* TODO */ },
        ) {
            Text("Retry")
        }
    }
}

@Preview
@Composable
private fun ErrorScreenPreview() {
    ErrorScreen(
        modifier = Modifier.fillMaxSize(),
    )
}
