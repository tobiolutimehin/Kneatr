package com.hollowvyn.kneatr.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object ContactsList : NavKey

data class ContactDetail(
    val id: String,
) : NavKey
