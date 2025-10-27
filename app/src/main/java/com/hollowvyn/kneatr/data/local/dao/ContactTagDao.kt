package com.hollowvyn.kneatr.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.hollowvyn.kneatr.data.local.entity.ContactTagEntity
import com.hollowvyn.kneatr.data.local.entity.crossRef.ContactTagCrossRef
import com.hollowvyn.kneatr.data.local.entity.relation.ContactWithTags
import com.hollowvyn.kneatr.data.local.entity.relation.TagWithContacts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Dao
interface ContactTagDao {
    @Query("SELECT * FROM tags")
    fun getAllTags(): Flow<List<ContactTagEntity>>

    @Transaction
    @Query("SELECT * FROM tags")
    fun getTagsWithContacts(): Flow<List<TagWithContacts>>

    @Transaction
    @Query("SELECT * FROM tags WHERE tagId = :id")
    fun getTagWithContactsById(id: Long): Flow<TagWithContacts?>

    @Transaction
    @Query("SELECT * FROM contacts WHERE contactId = :id")
    fun getContactWithTagsById(id: Long): Flow<ContactWithTags?>

    @Transaction
    @Query("SELECT * FROM tags WHERE name = :name")
    fun getTagWithContactsByName(name: String): Flow<TagWithContacts?>

    @Query("SELECT * FROM tags WHERE tagId = :id")
    fun getTagById(id: Long): Flow<ContactTagEntity?>

    @Query("SELECT * FROM tags WHERE name = :name")
    fun getTagByName(name: String): Flow<ContactTagEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: ContactTagEntity)

    @Update
    suspend fun updateTag(tag: ContactTagEntity)

    @Delete
    suspend fun deleteTag(tag: ContactTagEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTags(tags: List<ContactTagEntity>): List<Long>

    @Transaction
    suspend fun updateContactTags(
        contactId: Long,
        newTags: List<ContactTagEntity>,
    ) {
        val presentContactTags = getContactWithTagsById(contactId).first()
        val allTags = getAllTags().first()

        val tagsToInsert =
            newTags.filter { newTag ->
                allTags.none { existingTag -> existingTag.name == newTag.name }
            }

        val existingTagsToUse =
            newTags.mapNotNull { newTag ->
                allTags.find { existingTag -> existingTag.name == newTag.name }
            }

        val tagsToDelete =
            presentContactTags?.tags?.filter { currentTag ->
                newTags.none { newTag -> newTag.name == currentTag.name }
            } ?: emptyList()

        val insertedTagIds = insertTags(tagsToInsert)

        if (tagsToDelete.isNotEmpty()) {
            deleteContactTagsByContactId(contactId, tagsToDelete.map { it.tagId })
        }

        val finalTagIds = existingTagsToUse.map { it.tagId } + insertedTagIds

        val newCrossReferences =
            finalTagIds.map { tagId ->
                ContactTagCrossRef(contactId, tagId)
            }

        insertContactTagCrossReferences(newCrossReferences)
    }

    @Query("DELETE FROM ContactTagCrossRef WHERE contactId = :contactId")
    suspend fun deleteContactTagsByContactId(contactId: Long)

    @Query("DELETE FROM ContactTagCrossRef WHERE contactId = :contactId AND tagId IN (:tagIds)")
    suspend fun deleteContactTagsByContactId(
        contactId: Long,
        tagIds: List<Long>?,
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContactTagCrossReferences(crossRefs: List<ContactTagCrossRef>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTagsToContact(crossRefs: List<ContactTagCrossRef>)
}
