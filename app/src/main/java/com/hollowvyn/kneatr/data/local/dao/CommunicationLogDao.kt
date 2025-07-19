package com.hollowvyn.kneatr.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hollowvyn.kneatr.data.local.entity.CommunicationLogEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface CommunicationLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunicationLog(communicationLog: CommunicationLogEntity)

    @Query("SELECT * FROM communication_logs WHERE contactId = :contactId")
    fun getCommunicationLogsForContact(contactId: Int): Flow<List<CommunicationLogEntity>>

    @Delete
    suspend fun deleteCommunicationLog(communicationLog: CommunicationLogEntity)

    @Query("DELETE FROM communication_logs WHERE contactId = :contactId")
    suspend fun deleteCommunicationLogsForContact(contactId: Int)

    @Query("DELETE FROM communication_logs")
    suspend fun deleteAllCommunicationLogs()

    @Query("SELECT * FROM communication_logs")
    fun getAllCommunicationLogs(): Flow<List<CommunicationLogEntity>>

    @Query("SELECT * FROM communication_logs WHERE date = :date")
    fun getCommunicationLogsByDate(date: LocalDate): Flow<List<CommunicationLogEntity>>

    @Update
    suspend fun updateCommunicationLog(log: CommunicationLogEntity)
}
