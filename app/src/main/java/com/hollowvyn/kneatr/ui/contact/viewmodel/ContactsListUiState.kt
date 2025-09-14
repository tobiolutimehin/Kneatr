package com.hollowvyn.kneatr.ui.contact.viewmodel

import com.hollowvyn.kneatr.domain.model.ContactDto
import javax.annotation.concurrent.Immutable

@Immutable
sealed interface ContactsListUiState {
    data class Success(
        val contacts: List<ContactDto>,
        val searchedContacts: List<ContactDto>,
        val query: String,
    ) : ContactsListUiState

    data object Error : ContactsListUiState

    data object Loading : ContactsListUiState

    data object Empty : ContactsListUiState
}
