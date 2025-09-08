package com.hollowvyn.kneatr.ui.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.hollowvyn.kneatr.data.CONTACT_SYNC_TAG
import com.hollowvyn.kneatr.data.repository.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ContactsListViewModel
    @Inject
    constructor(
        contactsRepository: ContactsRepository,
        workManager: WorkManager,
    ) : ViewModel() {
        private val workInfoFlow = workManager.getWorkInfosByTagFlow(CONTACT_SYNC_TAG)
        private val contactsFlow = contactsRepository.getAllContacts()

        val uiState: StateFlow<ContactsListUiState> =
            combine(contactsFlow, workInfoFlow) { contacts, workInfoList ->
                val isWorkerRunning =
                    workInfoList.any {
                        it.state == WorkInfo.State.RUNNING || it.state == WorkInfo.State.ENQUEUED
                    }

                val didWorkerFail = workInfoList.any { it.state == WorkInfo.State.FAILED }

                if (isWorkerRunning && contacts.isEmpty()) {
                    ContactsListUiState.Loading
                } else if (contacts.isNotEmpty()) {
                    ContactsListUiState.Success(contacts)
                } else if (didWorkerFail) {
                    ContactsListUiState.Error
                } else {
                    ContactsListUiState.Empty
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
            initialValue = ContactsListUiState.Loading,
        )
    }
