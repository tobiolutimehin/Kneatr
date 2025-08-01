package com.hollowvyn.kneatr.domain.mappers

import com.hollowvyn.kneatr.data.local.entity.ContactTierEntity
import com.hollowvyn.kneatr.domain.model.ContactTierDto

fun ContactTierDto.toEntity(): ContactTierEntity =
    ContactTierEntity(
        tierId = id,
        name = name,
        daysBetweenContact = daysBetweenContact,
    )

fun ContactTierEntity.toModel(): ContactTierDto =
    ContactTierDto(
        id = tierId,
        name = name,
        daysBetweenContact = daysBetweenContact,
    )
