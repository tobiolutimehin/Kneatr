package com.hollowvyn.kneatr.domain.mappers

import com.hollowvyn.kneatr.data.local.entity.ContactTagEntity
import com.hollowvyn.kneatr.domain.model.ContactTagDto

fun ContactTagDto.toEntity(): ContactTagEntity =
    ContactTagEntity(
        tagId = id,
        name = name,
    )

fun ContactTagEntity.toModel(): ContactTagDto =
    ContactTagDto(
        id = tagId,
        name = name,
    )

fun List<ContactTagEntity>.toModelSet(): Set<ContactTagDto> = map { it.toModel() }.toSet()
