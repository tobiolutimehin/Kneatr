package com.hollowvyn.kneatr.ui.contact.viewmodel

import com.hollowvyn.kneatr.domain.model.Contact
import javax.annotation.concurrent.Immutable

@Immutable
sealed interface ContactsListUiState {
    data class Success(
        val contacts: List<Contact>,
        val searchedContacts: List<Contact>,
        val query: String,
    ) : ContactsListUiState

    data object Error : ContactsListUiState

    data object Loading : ContactsListUiState

    data object Empty : ContactsListUiState
}
