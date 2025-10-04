package com.hollowvyn.kneatr.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey

sealed interface TopLevelRoute : NavKey {
    val icon: ImageVector

    companion object {
        val entries: List<TopLevelRoute>
            get() =
                TopLevelRoute::class
                    .sealedSubclasses
                    .mapNotNull { it.objectInstance }
    }
}
