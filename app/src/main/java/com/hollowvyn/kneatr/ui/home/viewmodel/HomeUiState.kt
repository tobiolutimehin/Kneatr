package com.hollowvyn.kneatr.ui.home.viewmodel

import com.hollowvyn.kneatr.domain.model.Contact

sealed interface HomeUiState {
    data class Success(
        val overdueContacts: List<Contact>,
        val randomContacts: List<Contact>,
        val upcomingContacts: List<Contact>,
        val contactDueToday: List<Contact>,
    ) : HomeUiState

    object Error : HomeUiState

    object Loading : HomeUiState
}
