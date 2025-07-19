package com.hollowvyn.kneatr.data.local.dao.fakes

import com.hollowvyn.kneatr.data.local.dao.ContactTierDao
import com.hollowvyn.kneatr.data.local.entity.ContactTierEntity
import com.hollowvyn.kneatr.data.local.entity.relation.ContactWithDetails
import com.hollowvyn.kneatr.data.local.entity.relation.TierWithContacts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeContactTierDao : ContactTierDao {
    private val tiers = mutableListOf<ContactTierEntity>()
    private val tiersFlow = MutableStateFlow<List<ContactTierEntity>>(emptyList())

    // Map tierId to contacts for TierWithContacts relation
    private val contactsByTierId = mutableMapOf<Long, List<ContactWithDetails>>()

    private fun updateFlow() {
        tiersFlow.value = tiers.toList()
    }

    override suspend fun insertTiers(tiers: List<ContactTierEntity>) {
        tiers.forEach { tier ->
            this.tiers.removeAll { it.tierId == tier.tierId }
            this.tiers.add(tier)
        }
        updateFlow()
    }

    override suspend fun insertTier(tier: ContactTierEntity) {
        tiers.removeAll { it.tierId == tier.tierId }
        tiers.add(tier)
        updateFlow()
    }

    override suspend fun updateTier(tier: ContactTierEntity) {
        val idx = tiers.indexOfFirst { it.tierId == tier.tierId }
        if (idx != -1) {
            tiers[idx] = tier
            updateFlow()
        }
    }

    override fun getAllTiers(): Flow<List<ContactTierEntity>> = tiersFlow

    override fun getTierById(id: Long): Flow<ContactTierEntity?> = tiersFlow.map { list -> list.find { it.tierId == id } }

    override suspend fun deleteAllTiers() {
        tiers.clear()
        updateFlow()
    }

    override fun getTiersWithContacts(): Flow<List<TierWithContacts>> =
        tiersFlow.map { currentTiers ->
            currentTiers.map { tier ->
                TierWithContacts(
                    tier = tier,
                    contacts = emptyList(),
                )
            }
        }

    override fun getTierWithContactsById(id: Long): Flow<TierWithContacts?> =
        getTiersWithContacts().map { list -> list.find { it.tier.tierId == id } }

    override fun getTierWithContactsByName(name: String): Flow<TierWithContacts?> =
        getTiersWithContacts().map { list -> list.find { it.tier.name == name } }

    // Utility:
    fun setContactsForTier(
        tierId: Long,
        contacts: List<ContactWithDetails>,
    ) {
        contactsByTierId[tierId] = contacts
    }
}
