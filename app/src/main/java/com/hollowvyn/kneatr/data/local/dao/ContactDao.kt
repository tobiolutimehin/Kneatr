package com.hollowvyn.kneatr.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.hollowvyn.kneatr.data.local.entity.ContactEntity
import com.hollowvyn.kneatr.data.local.entity.relation.ContactWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contacts: List<ContactEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactEntity)

    @Update
    suspend fun updateContact(contact: ContactEntity)

    @Transaction
    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAllContacts(): Flow<List<ContactWithDetails>>

    @Transaction
    @Query("SELECT * FROM contacts ORDER BY name ASC")
    suspend fun getAllContactsAtOnce(): List<ContactWithDetails>

    @Transaction
    @Query("SELECT * FROM contacts WHERE tierId = :tierId ORDER BY name ASC")
    fun getContactsByTierId(tierId: Long): Flow<List<ContactWithDetails>>

    @Transaction
    @Query(
        """
        SELECT * FROM contacts
        WHERE name LIKE '%' || :query || '%' OR phoneNumber LIKE '%' || :query || '%' OR email LIKE '%' || :query || '%'
        ORDER BY name ASC
    """,
    )
    fun searchContactsByNamePhoneOrEmail(query: String): Flow<List<ContactWithDetails>>

    @Transaction
    @Query("SELECT * FROM contacts WHERE contactId = :id")
    fun getContactById(id: Long): Flow<ContactWithDetails?>

    @Transaction
    @Query(
        """
    SELECT * FROM contacts
    WHERE contactId IN (
        SELECT contactId FROM ContactTagCrossRef WHERE tagId = :tagId
    )
    ORDER BY name ASC
""",
    )
    fun getContactsByTagId(tagId: Long): Flow<List<ContactWithDetails>>

    @Query("DELETE FROM contacts WHERE contactId = :id")
    suspend fun deleteContactById(id: Long)
}
