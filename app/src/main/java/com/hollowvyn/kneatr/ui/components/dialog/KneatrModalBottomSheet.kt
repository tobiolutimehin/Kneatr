package com.hollowvyn.kneatr.ui.components.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KneatrModalBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (hideSheet: () -> Unit) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val hideSheet: () -> Unit = {
        scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
            if (!bottomSheetState.isVisible) {
                onDismiss()
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        contentWindowInsets = { BottomSheetDefaults.windowInsets },
        modifier = modifier,
        sheetGesturesEnabled = false,
    ) {
        content(hideSheet)
    }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun KneatrModalBottomSheetPreview() {
    KneatrModalBottomSheet(onDismiss = { }) { _ ->
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(200.dp),
        ) {
            Button(onClick = { }) {
                Text("Hide sheet")
            }
        }
    }
}

