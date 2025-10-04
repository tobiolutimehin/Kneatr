package com.hollowvyn.kneatr.domain.mappers

import com.hollowvyn.kneatr.data.local.entity.ContactTagEntity
import com.hollowvyn.kneatr.domain.model.ContactTag

fun ContactTag.toEntity(): ContactTagEntity =
    ContactTagEntity(
        tagId = id,
        name = name,
    )

fun ContactTagEntity.toModel(): ContactTag =
    ContactTag(
        id = tagId,
        name = name,
    )

fun List<ContactTagEntity>.toModelSet(): Set<ContactTag> = map { it.toModel() }.toSet()
