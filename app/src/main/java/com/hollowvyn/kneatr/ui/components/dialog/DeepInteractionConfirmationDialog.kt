package com.hollowvyn.kneatr.ui.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hollowvyn.kneatr.R

@Composable
fun DeepInteractionConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    text: String,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.are_you_sure)) },
        text = { Text(text) },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    onConfirm()
                },
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
    )
}

@Preview
@Composable
private fun DeepInteractionConfirmationDialogPreview() {
    DeepInteractionConfirmationDialog(
        onConfirm = {},
        onDismiss = {},
        text = "Are you sure you want to perform this action?",
    )
}
