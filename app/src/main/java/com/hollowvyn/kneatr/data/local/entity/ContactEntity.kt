package com.hollowvyn.kneatr.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "contacts",
    foreignKeys = [
        ForeignKey(
            entity = ContactTierEntity::class,
            parentColumns = ["tierId"],
            childColumns = ["tierId"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
)
data class ContactEntity(
    @PrimaryKey(autoGenerate = true) val contactId: Long,
    val name: String,
    val phoneNumber: String,
    val email: String? = null,
    val tierId: Long? = null,
    val customFrequencyDays: Int? = null,
)
