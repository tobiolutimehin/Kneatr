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
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Query("SELECT * FROM tags")
    fun getAllTags(): Flow<List<TagEntity>>

    @Transaction
    @Query("SELECT * FROM tags")
    fun getTagsWithContacts(): Flow<List<TagWithContacts>>

    @Transaction
    @Query("SELECT * FROM tags WHERE tagId = :id")
    fun getTagWithContactsById(id: Int): Flow<TagWithContacts?>

    @Transaction
    @Query("SELECT * FROM tags WHERE name = :name")
    fun getTagWithContactsByName(name: String): Flow<TagWithContacts?>

    @Query("SELECT * FROM tags WHERE tagId = :id")
    fun getTagById(id: Int): Flow<TagEntity?>

    @Query("SELECT * FROM tags WHERE name = :name")
    fun getTagByName(name: String): Flow<TagEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: TagEntity)

    @Update
    suspend fun updateTag(tag: TagEntity)

    @Delete
    suspend fun deleteTag(tag: TagEntity)
}
