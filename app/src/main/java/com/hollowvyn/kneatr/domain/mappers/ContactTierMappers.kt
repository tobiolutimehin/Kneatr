package com.hollowvyn.kneatr.domain.mappers

import com.hollowvyn.kneatr.data.local.entity.ContactTierEntity
import com.hollowvyn.kneatr.domain.model.ContactTier

fun ContactTier.toEntity(): ContactTierEntity =
    ContactTierEntity(
        tierId = id,
        name = name,
        daysBetweenContact = daysBetweenContact,
    )

fun ContactTierEntity.toModel(): ContactTier =
    ContactTier(
        id = tierId,
        name = name,
        daysBetweenContact = daysBetweenContact,
    )
