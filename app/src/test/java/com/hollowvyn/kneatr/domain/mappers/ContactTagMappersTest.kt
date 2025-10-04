package com.hollowvyn.kneatr.domain.mappers

import com.hollowvyn.kneatr.data.local.entity.ContactTagEntity
import com.hollowvyn.kneatr.domain.model.ContactTag
import kotlin.test.Test
import kotlin.test.assertEquals

class ContactTagMappersTest {
    @Test
    fun `ContactTagDto to ContactTagEntity and back mapping`() {
        val tagDto = ContactTag(id = 10L, name = "Family")
        val entity = tagDto.toEntity()
        assertEquals(tagDto.id, entity.tagId)
        assertEquals(tagDto.name, entity.name)

        val model = entity.toModel()
        assertEquals(entity.tagId, model.id)
        assertEquals(entity.name, model.name)
    }

    @Test
    fun `List of ContactTagEntity to Set of ContactTagDto mapping`() {
        val tagEntities =
            listOf(
                ContactTagEntity(tagId = 1L, name = "Tag1"),
                ContactTagEntity(tagId = 2L, name = "Tag2"),
            )
        val tagDtos = tagEntities.toModelSet()
        assertEquals(2, tagDtos.size)
        tagEntities.forEach { entity ->
            assert(tagDtos.any { it.id == entity.tagId && it.name == entity.name })
        }
    }
}
