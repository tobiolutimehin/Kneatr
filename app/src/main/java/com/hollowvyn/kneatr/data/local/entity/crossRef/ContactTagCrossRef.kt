package com.hollowvyn.kneatr.data.local.entity.crossRef

import androidx.room.Entity

@Entity(primaryKeys = ["contactId", "tagId"])
data class ContactTagCrossRef(
    val contactId: Long,
    val tagId: Long,
)
