package com.hollowvyn.kneatr.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList

class TopLevelBackStack<T : Any>(
    startKey: T,
) {
    // Maintain a stack for each top level route
    private var topLevelStacks: LinkedHashMap<T, SnapshotStateList<T>> =
        linkedMapOf(
            startKey to mutableStateListOf(startKey),
        )

    // Expose the current top level route for consumers
    var topLevelKey by mutableStateOf(startKey)
        private set

    // Expose the back stack so it can be rendered by the NavDisplay
    val backStack = mutableStateListOf(startKey)

    private fun updateBackStack() =
        backStack.apply {
            clear()
            // This now correctly flattens the map of stacks into a single list
            // The LinkedHashMap ensures the order is preserved (current tab is last)
            addAll(topLevelStacks.values.flatten())
        }

    fun addTopLevel(key: T) {
        // If the clicked key is already the current top level key...
        if (key == topLevelKey) {
            // ...pop its stack back to the root.
            topLevelStacks[key]?.let { stack ->
                stack.clear()
                stack.add(key)
            }
        } else {
            // If the top level doesn't exist, add it
            if (topLevelStacks[key] == null) {
                topLevelStacks[key] = mutableStateListOf(key)
            }
            // Move the stack associated with the key to the end to make it current
            topLevelStacks.remove(key)?.let {
                topLevelStacks[key] = it
            }
            topLevelKey = key
        }

        updateBackStack()
    }

    fun add(key: T) {
        topLevelStacks[topLevelKey]?.add(key)
        updateBackStack()
    }

    fun removeLast() {
        val currentStack = topLevelStacks[topLevelKey]

        // If we can pop from the current top-level stack without it being empty, just pop.
        if ((currentStack?.size ?: 0) > 1) {
            currentStack?.removeLastOrNull()
        } else {
            // Otherwise, we are at the root of a top-level stack. Remove it.
            if (topLevelStacks.size > 1) {
                topLevelStacks.remove(topLevelKey)
                topLevelKey = topLevelStacks.keys.last()
            }
        }
        updateBackStack()
    }
}
