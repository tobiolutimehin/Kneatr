package com.hollowvyn.kneatr.domain.mappers

import com.hollowvyn.kneatr.data.local.entity.CommunicationLogEntity
import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import com.hollowvyn.kneatr.domain.model.CommunicationLog
import kotlin.test.Test
import kotlin.test.assertEquals

class CommunicationLogMappersKtTest {
    private val sampleDate = kotlinx.datetime.LocalDate(2024, 1, 1)

    @Test
    fun `CommunicationLogDto to CommunicationLogEntity and back mapping`() {
        val commDto =
            CommunicationLog(
                id = 1L,
                type = CommunicationType.PHONE_CALL,
                date = sampleDate,
                contactId = 123,
                notes = "Called about meeting",
            )
        val entity = commDto.toEntity()
        assertEquals(commDto.id, entity.communicationId)
        assertEquals(commDto.type, entity.type)
        assertEquals(commDto.date, entity.date)
        assertEquals(commDto.contactId, entity.contactId)
        assertEquals(commDto.notes, entity.notes)

        val model = entity.toModel()
        assertEquals(entity.communicationId, model.id)
        assertEquals(entity.type, model.type)
        assertEquals(entity.date, model.date)
        assertEquals(entity.contactId, model.contactId)
        assertEquals(entity.notes, model.notes)
    }

    @Test
    fun `List of CommunicationLogEntity to List of CommunicationLogDto mapping`() {
        val commEntities =
            listOf(
                CommunicationLogEntity(
                    communicationId = 1L,
                    type = CommunicationType.EMAIL,
                    date = sampleDate,
                    contactId = 5,
                    notes = "Email about update",
                ),
                CommunicationLogEntity(
                    communicationId = 2L,
                    type = CommunicationType.MESSAGE,
                    date = sampleDate,
                    contactId = 5,
                    notes = null,
                ),
            )
        val dtos = commEntities.toModelList()
        assertEquals(2, dtos.size)
        commEntities.forEachIndexed { i, entity ->
            val dto = dtos[i]
            assertEquals(entity.communicationId, dto.id)
            assertEquals(entity.type, dto.type)
            assertEquals(entity.date, dto.date)
            assertEquals(entity.contactId, dto.contactId)
            assertEquals(entity.notes, dto.notes)
        }
    }
}
