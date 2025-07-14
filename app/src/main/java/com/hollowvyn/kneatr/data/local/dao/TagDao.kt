package com.hollowvyn.kneatr.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.hollowvyn.kneatr.data.local.entity.TagEntity
import com.hollowvyn.kneatr.data.local.entity.relation.TagWithContacts

@Dao
interface TagDao {
    @Query("SELECT * FROM tags")
    suspend fun getAllTags(): List<TagEntity>

    @Transaction
    @Query("SELECT * FROM tags")
    suspend fun getTagsWithContacts(): List<TagWithContacts>

    @Transaction
    @Query("SELECT * FROM tags WHERE tagId = :id")
    suspend fun getTagWithContactsById(id: Int): TagWithContacts?

    @Transaction
    @Query("SELECT * FROM tags WHERE name = :name")
    suspend fun getTagWithContactsByName(name: String): TagWithContacts?

    @Query("SELECT * FROM tags WHERE tagId = :id")
    suspend fun getTagById(id: Int): TagEntity?

    @Query("SELECT * FROM tags WHERE name = :name")
    suspend fun getTagByName(name: String): TagEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: TagEntity)

    @Update
    suspend fun updateTag(tag: TagEntity)

    @Delete
    suspend fun deleteTag(tag: TagEntity)
}
