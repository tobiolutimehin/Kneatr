package com.hollowvyn.kneatr.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.hollowvyn.kneatr.R
import kotlinx.serialization.Serializable

@Serializable
data object ContactsList :
    TopLevelRoute {
    override val icon: Int
        get() = R.drawable.contacts_24px

    override val label: Int
        get() = R.string.contacts

    override val contentDescription: Int
        get() = R.string.contacts_tab_content_description
}

@Serializable
data class ContactDetail(
    val id: Long,
) : NavKey
