package com.hollowvyn.kneatr.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation3.runtime.NavKey

sealed interface TopLevelRoute : NavKey {
    @get:DrawableRes
    val icon: Int

    @get:StringRes
    val label: Int

    @get:StringRes
    val contentDescription: Int

    companion object {
//        val entries: List<TopLevelRoute>
//            get() =
//                TopLevelRoute::class
//                    .sealedSubclasses
//                    .mapNotNull { it.objectInstance }

        val entries = listOf(Home, ContactsList)
    }
}
