package com.hollowvyn.kneatr.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.hollowvyn.kneatr.data.local.entity.ContactTierEntity
import com.hollowvyn.kneatr.data.local.entity.relation.TierWithContacts
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactTierDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTiers(tiers: List<ContactTierEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTier(tier: ContactTierEntity)

    @Update
    suspend fun updateTier(tier: ContactTierEntity)

    @Query("SELECT * FROM contact_tiers")
    fun getAllTiers(): Flow<List<ContactTierEntity>>

    @Query("SELECT * FROM contact_tiers WHERE tierId = :id")
    fun getTierById(id: Long): Flow<ContactTierEntity?>

    @Query("DELETE FROM contact_tiers")
    suspend fun deleteAllTiers()

    @Transaction
    @Query("SELECT * FROM contact_tiers")
    fun getTiersWithContacts(): Flow<List<TierWithContacts>>

    @Transaction
    @Query("SELECT * FROM contact_tiers WHERE tierId = :id")
    fun getTierWithContactsById(id: Long): Flow<TierWithContacts?>

    @Transaction
    @Query("SELECT * FROM contact_tiers WHERE name = :name")
    fun getTierWithContactsByName(name: String): Flow<TierWithContacts?>
}
