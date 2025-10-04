package com.hollowvyn.kneatr.ui.contact.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hollowvyn.kneatr.domain.repository.ContactsRepository
import com.hollowvyn.kneatr.ui.contact.ContactDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
    }
