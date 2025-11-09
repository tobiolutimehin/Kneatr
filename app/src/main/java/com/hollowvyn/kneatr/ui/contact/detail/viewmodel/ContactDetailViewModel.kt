package com.hollowvyn.kneatr.ui.contact.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import com.hollowvyn.kneatr.domain.model.CommunicationLog
import com.hollowvyn.kneatr.domain.model.ContactTag
import com.hollowvyn.kneatr.domain.model.ContactTier
import com.hollowvyn.kneatr.domain.repository.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@HiltViewModel
class ContactDetailViewModel
    @Inject
    constructor(
        private val contactRepository: ContactsRepository,
    ) : ViewModel() {
        private val contactIdFlow = MutableStateFlow<Long?>(null)
        val tiers: StateFlow<List<ContactTier>> =
            contactRepository
                .getAllTiers()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptyList(),
                )

        val allTags: StateFlow<Set<ContactTag>> =
            contactRepository
                .getAllTags()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptySet(),
                )

        @OptIn(ExperimentalCoroutinesApi::class)
        val uiState: StateFlow<ContactDetailUiState> =
            contactIdFlow
                .filterNotNull()
                .flatMapLatest { id ->
                    contactRepository
                        .getContactById(id)
                        .map { contact ->
                            contact?.let { ContactDetailUiState.Success(contact = contact) }
                                ?: ContactDetailUiState.Error
                        }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = ContactDetailUiState.Loading,
                )

        fun loadContactId(contactId: Long) {
            contactIdFlow.value = contactId
        }

        fun addCommunicationLog(
            date: LocalDate,
            type: CommunicationType,
            notes: String? = null,
        ) {
            viewModelScope.launch {
                contactIdFlow.value?.let { contactId ->
                    contactRepository.insertCommunicationLog(
                        log =
                            CommunicationLog(
                                contactId = contactId,
                                date = date,
                                type = type,
                                notes = notes,
                            ),
                    )
                }
            }
        }

        fun deleteCommunicationLog(log: CommunicationLog) {
            viewModelScope.launch {
                contactIdFlow.value?.let { contactId ->
                    contactRepository.deleteCommunicationLog(log)
                }
            }
        }

        fun updateCommunicationLog(
            date: LocalDate,
            type: CommunicationType,
            notes: String,
            logId: Long,
        ) {
            viewModelScope.launch {
                contactIdFlow.value?.let { contactId ->
                    contactRepository.updateCommunicationLog(
                        log =
                            CommunicationLog(
                                id = logId,
                                contactId = contactId,
                                date = date,
                                type = type,
                                notes = notes,
                            ),
                    )
                }
            }
        }

        fun updateTier(tier: ContactTier?) {
            viewModelScope.launch {
                uiState.value.let { state ->
                    if (state is ContactDetailUiState.Success) {
                        contactRepository.updateContact(contact = state.contact.copy(tier = tier))
                    }
                }
            }
        }

        fun updateTags(tags: Set<ContactTag>) {
            val currentState = uiState.value
            if (currentState is ContactDetailUiState.Success) {
                viewModelScope.launch {
                    contactRepository.addTagsToContact(
                        contactId = currentState.contact.id,
                        tagIds = tags.toMutableList(),
                    )
                }
        }
    }
    }
