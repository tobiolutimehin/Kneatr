package com.hollowvyn.kneatr.ui.contact

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hollowvyn.kneatr.data.repository.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsListViewModel
    @Inject
    constructor(
        private val contactsRepository: ContactsRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<ContactsListUiState>(ContactsListUiState.Empty)
        val uiState: StateFlow<ContactsListUiState> = _uiState.asStateFlow()

        fun getAllContacts() {
            viewModelScope.launch {
                _uiState.value = ContactsListUiState.Loading
                try {
                    val value = contactsRepository.getAllContacts().first()
                    Log.d("ContactsListViewModel", "getAllContacts in viewmodel: $value")
                    _uiState.value = ContactsListUiState.Success(value)
                } catch (_: Exception) {
                    _uiState.value = ContactsListUiState.Error
                }
            }
        }

        fun searchContacts(query: String) {
            viewModelScope.launch {
                try {
                    val value = contactsRepository.searchContactsByNamePhoneOrEmail(query)

                    if (_uiState.value is ContactsListUiState.Success) {
                        _uiState.update {
                            (it as ContactsListUiState.Success).copy(
                                searchedContacts = value,
                            )
                        }
                    }
                } catch (_: Exception) {
                    Log.e("ContactsListViewModel", "Error searching contacts")
                }
            }
        }

        init {
            getAllContacts()
        }
    }
