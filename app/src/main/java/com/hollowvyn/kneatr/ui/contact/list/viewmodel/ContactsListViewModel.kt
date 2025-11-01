
package com.hollowvyn.kneatr.ui.contact.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.hollowvyn.kneatr.data.worker.CONTACT_SYNC_TAG
import com.hollowvyn.kneatr.domain.repository.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ContactsListViewModel
    @Inject
    constructor(
        contactsRepository: ContactsRepository,
        workManager: WorkManager,
    ) : ViewModel() {
        private val workInfoFlow = workManager.getWorkInfosByTagFlow(CONTACT_SYNC_TAG)
        private val contactsFlow = contactsRepository.getAllContacts()
        private val _query = MutableStateFlow("")
        val query: StateFlow<String> = _query

        val searchedContacts =
            query.flatMapLatest { queryString ->
                contactsRepository.searchContactsByNamePhoneOrEmail(queryString)
            }

        val uiState: StateFlow<ContactsListUiState> =
            combine(
                contactsFlow,
                workInfoFlow,
                searchedContacts,
                query,
            ) { contacts, workInfoList, searchedContacts, queryString ->
                val isWorkerRunning =
                    workInfoList.any {
                        it.state == WorkInfo.State.RUNNING
                    }
                val didWorkerFail = workInfoList.any { it.state == WorkInfo.State.FAILED }

                if (didWorkerFail) {
                    ContactsListUiState.Error
                } else if (isWorkerRunning && contacts.isEmpty()) {
                    ContactsListUiState.Loading
                } else if (queryString.isNotEmpty() && searchedContacts.isNotEmpty()) {
                    ContactsListUiState.Success(contacts, searchedContacts, queryString)
                } else if (contacts.isNotEmpty()) {
                    ContactsListUiState.Success(contacts, searchedContacts, queryString)
                } else {
                    ContactsListUiState.Empty
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ContactsListUiState.Loading,
            )

        fun onQueryChange(newQuery: String) {
            _query.value = newQuery
        }
    }
