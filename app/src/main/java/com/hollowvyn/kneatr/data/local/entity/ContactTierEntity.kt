package com.hollowvyn.kneatr.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_tiers")
data class ContactTierEntity(
    @PrimaryKey(autoGenerate = true) val tierId: Int,
    val name: String,
    val daysBetweenContact: Int,
)
