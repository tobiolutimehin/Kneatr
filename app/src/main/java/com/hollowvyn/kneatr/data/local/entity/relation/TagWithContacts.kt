package com.hollowvyn.kneatr.data.local.entity.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.hollowvyn.kneatr.data.local.entity.ContactEntity
import com.hollowvyn.kneatr.data.local.entity.TagEntity
import com.hollowvyn.kneatr.data.local.entity.crossRef.ContactTagCrossRef

data class TagWithContacts(
    @Embedded val tag: TagEntity,
    @Relation(
        parentColumn = "tagId",
        entityColumn = "contactId",
        associateBy = Junction(ContactTagCrossRef::class),
    ) val contacts: List<ContactEntity>,
)
