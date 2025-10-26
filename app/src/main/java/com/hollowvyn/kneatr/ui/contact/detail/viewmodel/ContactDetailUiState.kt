package com.hollowvyn.kneatr.ui.contact.detail.viewmodel

import com.hollowvyn.kneatr.domain.model.Contact

sealed class ContactDetailUiState {
    data class Success(
        val contact: Contact,
    ) : ContactDetailUiState()

    data object Error : ContactDetailUiState()

    data object Loading : ContactDetailUiState()
}
