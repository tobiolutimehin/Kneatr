package com.hollowvyn.kneatr.domain.mappers

import com.hollowvyn.kneatr.domain.model.ContactTier
import kotlin.test.Test
import kotlin.test.assertEquals

class ContactTierMappersTest {
    @Test
    fun `ContactTierDto to ContactTierEntity and back mapping`() {
        val tierDto = ContactTier(id = 1L, name = "Tier1", daysBetweenContact = 30)
        val entity = tierDto.toEntity()
        assertEquals(tierDto.id, entity.tierId)
        assertEquals(tierDto.name, entity.name)
        assertEquals(tierDto.daysBetweenContact, entity.daysBetweenContact)

        val model = entity.toModel()
        assertEquals(entity.tierId, model.id)
        assertEquals(entity.name, model.name)
        assertEquals(entity.daysBetweenContact, model.daysBetweenContact)
    }
}
