package com.hollowvyn.kneatr.ui.navigation

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList

class TopLevelBackStack<T : Any>(
    startKey: T,
) {
    private var topLevelStacks: LinkedHashMap<T, SnapshotStateList<T>> =
        linkedMapOf(
            startKey to listOf(startKey).toMutableStateList(),
        )

    var topLevelKey by mutableStateOf(startKey)
        private set

    private val _backStack = mutableStateOf(listOf(startKey))

    val backStack: State<List<T>> = _backStack

    @Synchronized
    private fun updateBackStack() {
        _backStack.value = topLevelStacks.values.flatten()
    }

    @Synchronized
    fun addTopLevel(key: T) {
        if (key == topLevelKey) {
            return
        }
        if (topLevelStacks[key] == null) {
            topLevelStacks[key] = listOf(key).toMutableStateList()
        }
        // Move the stack associated with the key to the end to make it current
        topLevelStacks.remove(key)?.let {
            topLevelStacks[key] = it
        }
        topLevelKey = key

        updateBackStack()
    }

    @Synchronized
    fun add(key: T) {
        // Prevent adding duplicate screens at the top of the stack
        if (topLevelStacks[topLevelKey]?.lastOrNull() == key) {
            return
        }

        if (topLevelStacks[topLevelKey]?.last()?.javaClass == key.javaClass) {
            topLevelStacks[topLevelKey]?.removeLastOrNull()
        }
        topLevelStacks[topLevelKey]?.add(key)
        updateBackStack()
    }

    @Synchronized
    fun removeLast() {
        val currentStack = topLevelStacks[topLevelKey]

        if ((currentStack?.size ?: 0) > 1) {
            currentStack?.removeLastOrNull()
        } else {
            if (topLevelStacks.size > 1) {
                topLevelStacks.remove(topLevelKey)
                topLevelKey = topLevelStacks.keys.last()
            }
        }
        updateBackStack()
    }
}
