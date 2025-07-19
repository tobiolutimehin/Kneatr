package com.hollowvyn.kneatr.data.local.dao.fakes

import com.hollowvyn.kneatr.data.local.dao.CommunicationLogDao
import com.hollowvyn.kneatr.data.local.entity.CommunicationLogEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class FakeCommunicationLogDao : CommunicationLogDao {
    private val logs = mutableListOf<CommunicationLogEntity>()
    private val logsFlow = MutableStateFlow<List<CommunicationLogEntity>>(emptyList())

    override suspend fun insertCommunicationLog(communicationLog: CommunicationLogEntity) {
        logs.removeAll { it.communicationId == communicationLog.communicationId }
        logs.add(communicationLog)
        logsFlow.value = logs.toList()
    }

    override fun getCommunicationLogsForContact(contactId: Long): Flow<List<CommunicationLogEntity>> =
        logsFlow.map { it.filter { log -> log.contactId == contactId } }

    override suspend fun deleteCommunicationLog(communicationLog: CommunicationLogEntity) {
        logs.removeIf { it.communicationId == communicationLog.communicationId }
        logsFlow.value = logs.toList()
    }

    override suspend fun deleteCommunicationLogsForContact(contactId: Long) {
        logs.removeIf { it.contactId == contactId }
        logsFlow.value = logs.toList()
    }

    override suspend fun deleteAllCommunicationLogs() {
        logs.clear()
        logsFlow.value = emptyList()
    }

    override fun getAllCommunicationLogs(): Flow<List<CommunicationLogEntity>> = logsFlow

    override fun getCommunicationLogsByDate(date: LocalDate): Flow<List<CommunicationLogEntity>> =
        logsFlow.map { it.filter { log -> log.date == date } }

    override suspend fun updateCommunicationLog(log: CommunicationLogEntity) {
        val index = logs.indexOfFirst { it.communicationId == log.communicationId }
        if (index >= 0) {
            logs[index] = log
            logsFlow.value = logs.toList()
        }
    }
}
