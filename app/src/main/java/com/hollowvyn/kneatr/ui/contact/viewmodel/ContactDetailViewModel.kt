package com.hollowvyn.kneatr.ui.contact.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import com.hollowvyn.kneatr.domain.model.CommunicationLog
import com.hollowvyn.kneatr.domain.repository.ContactsRepository
import com.hollowvyn.kneatr.ui.contact.detail.ContactDetailUiState
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

        @OptIn(ExperimentalCoroutinesApi::class)
        val uiState: StateFlow<ContactDetailUiState> =
            contactIdFlow
                .filterNotNull()
                .flatMapLatest { id ->
                    contactRepository
                        .getContactById(id)
                        .map { contact ->
                            ContactDetailUiState.Success(contact = contact)
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
    }
