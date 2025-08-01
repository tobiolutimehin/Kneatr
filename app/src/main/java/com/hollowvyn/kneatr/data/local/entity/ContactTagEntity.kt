package com.hollowvyn.kneatr.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class ContactTagEntity(
    @PrimaryKey(autoGenerate = true) val tagId: Long,
    val name: String,
)
