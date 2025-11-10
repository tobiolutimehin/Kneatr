package com.hollowvyn.kneatr.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

const val DEBOUNCE_TIMEOUT_MS = 1000L

/**
 * Remembers and returns a debounced click handler.
 *
 * This utility prevents a click action from being executed multiple times within a specified
 * timeout period. When the returned lambda is invoked, it will only execute the provided
 * [onClick] action if the timeout has passed since the last execution.
 *
 * @param onClick The action to be executed on a debounced click.
 * @param timeoutMillis The duration in milliseconds to wait before allowing another click.
 * @return A debounced lambda that can be passed to an `onClick` parameter.
 */
@Composable
fun rememberDebouncedOnClick(
    timeoutMillis: Long = DEBOUNCE_TIMEOUT_MS,
    onClick: () -> Unit,
): () -> Unit {
    var isClickEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(isClickEnabled) {
        if (!isClickEnabled) {
            delay(timeoutMillis)
            isClickEnabled = true
        }
    }

    return {
        if (isClickEnabled) {
            isClickEnabled = false
            onClick()
        }
    }
}
