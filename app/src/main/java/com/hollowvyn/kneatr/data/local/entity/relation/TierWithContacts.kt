package com.hollowvyn.kneatr.data.local.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.hollowvyn.kneatr.data.local.entity.ContactEntity
import com.hollowvyn.kneatr.data.local.entity.ContactTierEntity

data class TierWithContacts(
    @Embedded val tier: ContactTierEntity,
    @Relation(
        parentColumn = "tierId",
        entityColumn = "tierId",
    ) val contacts: List<ContactEntity>,
)
