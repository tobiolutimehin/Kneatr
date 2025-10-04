package com.hollowvyn.kneatr.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object ContactsList :
    TopLevelRoute {
    override val icon: ImageVector
        get() = Icons.Default.Home
}

@Serializable
data class ContactDetail(
    val id: Long,
) : NavKey

sealed interface TopLevelRoute : NavKey {
    val icon: ImageVector
}

val TOP_LEVEL_ROUTES: List<TopLevelRoute>
    get() =
        TopLevelRoute::class
            .sealedSubclasses
            .mapNotNull { it.objectInstance }
