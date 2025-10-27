package com.hollowvyn.kneatr.data.local.entity.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.hollowvyn.kneatr.data.local.entity.ContactEntity
import com.hollowvyn.kneatr.data.local.entity.ContactTagEntity
import com.hollowvyn.kneatr.data.local.entity.crossRef.ContactTagCrossRef

data class ContactWithTags(
    @Embedded val contact: ContactEntity,
    @Relation(
        parentColumn = "contactId",
        entityColumn = "tagId",
        associateBy = Junction(ContactTagCrossRef::class),
    ) val tags: List<ContactTagEntity>,
)
