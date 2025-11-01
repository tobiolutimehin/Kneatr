
package com.hollowvyn.kneatr.ui.contact.list.viewmodel

import com.hollowvyn.kneatr.domain.model.Contact
import javax.annotation.concurrent.Immutable

@Immutable
sealed interface ContactsListUiState {
    data class Success(
        val contacts: List<Contact>,
        val searchedContacts: List<Contact>,
        val query: String,
    ) : ContactsListUiState {
        val groupedContacts: Map<String, List<Contact>> =
            contacts.groupBy {
                if (it.name[0].isLetter()) {
                    it.name[0].uppercase()
                } else {
                    "#"
                }
            }
    }

    data object Error : ContactsListUiState

    data object Loading : ContactsListUiState

    data object Empty : ContactsListUiState
}
