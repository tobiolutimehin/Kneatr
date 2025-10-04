package com.hollowvyn.kneatr.domain.mappers

import com.hollowvyn.kneatr.data.local.entity.CommunicationLogEntity
import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import com.hollowvyn.kneatr.data.local.entity.ContactEntity
import com.hollowvyn.kneatr.data.local.entity.ContactTagEntity
import com.hollowvyn.kneatr.data.local.entity.ContactTierEntity
import com.hollowvyn.kneatr.data.local.entity.relation.ContactWithDetails
import com.hollowvyn.kneatr.domain.DateFakes.dateTimeHelper
import com.hollowvyn.kneatr.domain.model.Contact
import kotlin.test.Test
import kotlin.test.assertEquals

class ContactMappersTest {
    private val sampleDate = kotlinx.datetime.LocalDate(2024, 1, 1)

    @Test
    fun `ContactDto to ContactEntity and back mappings`() {
        val dto =
            Contact(
                id = 10L,
                name = "Alice",
                phoneNumber = "1234567890",
                email = "alice@example.com",
                customFrequencyDays = 7,
            )

        val entity = dto.toEntity()
        assertEquals(dto.id, entity.contactId)
        assertEquals(dto.name, entity.name)
        assertEquals(dto.phoneNumber, entity.phoneNumber)
        assertEquals(dto.email, entity.email)
        assertEquals(dto.customFrequencyDays, entity.customFrequencyDays)
    }

    @Test
    fun `ContactWithDetails to ContactDto mapping`() {
        // Prepare sample entities to map from
        val contactEntity =
            ContactEntity(
                contactId = 1L,
                name = "Bob",
                phoneNumber = "0987654321",
                email = "bob@example.com",
                customFrequencyDays = 14,
            )
        val tagEntities =
            listOf(
                ContactTagEntity(tagId = 2L, name = "Friend"),
                ContactTagEntity(tagId = 3L, name = "Work"),
            )
        val commEntities =
            listOf(
                CommunicationLogEntity(
                    communicationId = 4L,
                    type = CommunicationType.EMAIL,
                    date = sampleDate,
                    contactId = 1,
                    notes = "Discussed project",
                ),
            )
        val tierEntity =
            ContactTierEntity(
                tierId = 5L,
                name = "Close",
                daysBetweenContact = 7,
            )
        val contactWithDetails =
            ContactWithDetails(
                contact = contactEntity,
                tags = tagEntities,
                communicationLogs = commEntities,
                tier = tierEntity,
            )

        val dto = contactWithDetails.toModel(dateTimeHelper)

        assertEquals(contactEntity.contactId, dto.id)
        assertEquals(contactEntity.name, dto.name)
        assertEquals(contactEntity.phoneNumber, dto.phoneNumber)
        assertEquals(contactEntity.email, dto.email)
        assertEquals(contactEntity.customFrequencyDays, dto.customFrequencyDays)

        assertEquals(tagEntities.size, dto.tags.size)
        tagEntities.forEachIndexed { index, tagEntity ->
            val tagDto = dto.tags.elementAt(index)
            assertEquals(tagEntity.tagId, tagDto.id)
            assertEquals(tagEntity.name, tagDto.name)
        }

        assertEquals(commEntities.size, dto.communicationLogs.size)
        commEntities.forEachIndexed { index, commEntity ->
            val commDto = dto.communicationLogs[index]
            assertEquals(commEntity.communicationId, commDto.id)
            assertEquals(commEntity.type, commDto.type)
            assertEquals(commEntity.date, commDto.date)
            assertEquals(commEntity.contactId, commDto.contactId)
            assertEquals(commEntity.notes, commDto.notes)
        }

        assertEquals(tierEntity.tierId, dto.tier?.id)
        assertEquals(tierEntity.name, dto.tier?.name)
        assertEquals(tierEntity.daysBetweenContact, dto.tier?.daysBetweenContact)
    }
}
