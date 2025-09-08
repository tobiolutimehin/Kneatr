package com.hollowvyn.kneatr.ui.contact

import com.hollowvyn.kneatr.domain.model.ContactDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.annotation.concurrent.Immutable

@Immutable
sealed interface ContactsListUiState {
    data class Success(
        val contacts: List<ContactDto>,
        val searchedContacts: Flow<List<ContactDto>> = emptyFlow(),
    ) : ContactsListUiState

    data object Error : ContactsListUiState

    data object Loading : ContactsListUiState

    data object Empty : ContactsListUiState
}
