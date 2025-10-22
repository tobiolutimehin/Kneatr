package com.hollowvyn.kneatr.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class ContactTier(
    val id: Long,
    val name: String,
    val daysBetweenContact: Int,
)
