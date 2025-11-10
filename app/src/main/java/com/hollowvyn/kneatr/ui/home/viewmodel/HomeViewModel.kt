package com.hollowvyn.kneatr.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import com.hollowvyn.kneatr.domain.model.CommunicationLog
import com.hollowvyn.kneatr.domain.model.RelativeDate
import com.hollowvyn.kneatr.domain.repository.ContactsRepository
import com.hollowvyn.kneatr.domain.util.DateTimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val contactRepository: ContactsRepository,
    ) : ViewModel() {
        private val _forceRefreshRandom = MutableStateFlow(false)

        private val randomContactsFlow =
            _forceRefreshRandom.flatMapLatest { force ->
                contactRepository.getRandomHomeContacts(forceRefresh = force)
            }

        val uiState: StateFlow<HomeUiState> =
            combine(
                contactRepository.getOverdueContacts(),
                contactRepository.getUpcomingContacts(RelativeDate.Weeks(2)),
                contactRepository.getContactsDueToday(),
                randomContactsFlow,
            ) { overdueContacts, upcomingContacts, contactDueToday, randomContacts ->

                _forceRefreshRandom.value = false
                if (
                    overdueContacts.isEmpty() &&
                    randomContacts.isEmpty() &&
                    upcomingContacts.isEmpty() &&
                    contactDueToday.isEmpty()
                ) {
                    HomeUiState.Error
                } else {
                    HomeUiState.Success(
                        overdueContacts = overdueContacts,
                        randomContacts = randomContacts,
                        upcomingContacts = upcomingContacts,
                        contactDueToday = contactDueToday,
                    )
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HomeUiState.Loading,
            )

        fun markContactAsCompleted(contactId: Long) {
            viewModelScope.launch {
                contactRepository.insertCommunicationLog(
                    log =
                        CommunicationLog(
                            contactId = contactId,
                            date = DateTimeUtils.today(),
                            type = CommunicationType.OTHER,
                            notes = null,
                        ),
                )
            }
        }

    fun refreshRandomContacts() {
        viewModelScope.launch {
            delay(200)
            _forceRefreshRandom.value = true
        }
    }

    fun refreshContacts(section: HomeScreenSection) {
        when (section) {
            HomeScreenSection.Random -> {
                refreshRandomContacts()
            }

            else -> {}
        }
    }
    }
