package com.hollowvyn.kneatr.data.local.entity.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.hollowvyn.kneatr.data.local.entity.CommunicationLogEntity
import com.hollowvyn.kneatr.data.local.entity.ContactEntity
import com.hollowvyn.kneatr.data.local.entity.ContactTagEntity
import com.hollowvyn.kneatr.data.local.entity.ContactTierEntity
import com.hollowvyn.kneatr.data.local.entity.crossRef.ContactTagCrossRef

data class ContactWithDetails(
    @Embedded val contact: ContactEntity,
    @Relation(
        parentColumn = "contactId",
        entityColumn = "tagId",
        associateBy = Junction(ContactTagCrossRef::class),
    )
    val tags: List<ContactTagEntity>,
    @Relation(
        parentColumn = "tierId",
        entityColumn = "tierId",
    )
    val tier: ContactTierEntity?,
    @Relation(
        parentColumn = "contactId",
        entityColumn = "contactId",
    )
    val communicationLogEntities: List<CommunicationLogEntity>,
)
