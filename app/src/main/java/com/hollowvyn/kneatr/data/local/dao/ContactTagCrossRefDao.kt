package com.hollowvyn.kneatr.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hollowvyn.kneatr.data.local.entity.crossRef.ContactTagCrossRef

@Dao
interface ContactTagCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTagToContact(crossRef: ContactTagCrossRef)

    @Delete
    suspend fun removeTagFromContact(crossRef: ContactTagCrossRef)

    @Query("DELETE FROM ContactTagCrossRef WHERE contactId = :contactId")
    suspend fun removeAllTagsFromContact(contactId: Long)
}
