package com.hollowvyn.kneatr.data.local.entity.crossRef

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["contactId", "tagId"])
data class ContactTagCrossRef(
    val contactId: Long,
    @ColumnInfo(index = true)
    val tagId: Long,
)
