package com.hollowvyn.kneatr.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.hollowvyn.kneatr.data.local.entity.ContactTierEntity
import com.hollowvyn.kneatr.data.local.entity.relation.TierWithContacts

@Dao
interface ContactTierDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTiers(tiers: List<ContactTierEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTier(tier: ContactTierEntity)

    @Update
    suspend fun updateTier(tier: ContactTierEntity)

    @Query("SELECT * FROM contact_tiers")
    suspend fun getAllTiers(): List<ContactTierEntity>

    @Query("SELECT * FROM contact_tiers WHERE tierId = :id")
    suspend fun getTierById(id: Int): ContactTierEntity?

    @Query("DELETE FROM contact_tiers")
    suspend fun deleteAllTiers()

    @Transaction
    @Query("SELECT * FROM contact_tiers")
    suspend fun getTiersWithContacts(): List<TierWithContacts>

    @Transaction
    @Query("SELECT * FROM contact_tiers WHERE tierId = :id")
    suspend fun getTierWithContactsById(id: Int): TierWithContacts?

    @Transaction
    @Query("SELECT * FROM contact_tiers WHERE name = :name")
    suspend fun getTierWithContactsByName(name: String): TierWithContacts?
}
