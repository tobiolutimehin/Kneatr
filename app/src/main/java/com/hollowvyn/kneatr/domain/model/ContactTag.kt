package com.hollowvyn.kneatr.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class ContactTag(
    val id: Long = 0,
    val name: String,
)
