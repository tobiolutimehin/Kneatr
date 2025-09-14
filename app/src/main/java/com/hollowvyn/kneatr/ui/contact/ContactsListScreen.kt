package com.hollowvyn.kneatr.ui.contact

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hollowvyn.kneatr.domain.model.ContactDto
import com.hollowvyn.kneatr.ui.components.screenstates.EmptyScreen
import com.hollowvyn.kneatr.ui.components.screenstates.ErrorScreen
import com.hollowvyn.kneatr.ui.components.screenstates.LoadingScreen
import com.hollowvyn.kneatr.ui.contact.viewmodel.ContactsListUiState
import com.hollowvyn.kneatr.ui.contact.viewmodel.ContactsListViewModel

@Composable
fun ContactsListScreen(
    modifier: Modifier = Modifier,
    viewModel: ContactsListViewModel = hiltViewModel<ContactsListViewModel>(),
    onContactClick: (ContactDto) -> Unit = {},
) {
    val uiStateDelegate by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiStateDelegate) {
        is ContactsListUiState.Success -> {
            (uiStateDelegate as ContactsListUiState.Success).let { success ->
                ContactsListSuccessContent(
                    contacts = success.contacts,
                    modifier = modifier,
                    onContactClick = onContactClick,
                    searchedContacts = success.searchedContacts,
                    query = success.query,
                    onQueryChange = viewModel::onQueryChange,
                )
            }
        }

        is ContactsListUiState.Error -> {
            ErrorScreen(modifier = modifier)
        }

        is ContactsListUiState.Loading -> {
            LoadingScreen(modifier = modifier)
        }

        is ContactsListUiState.Empty -> {
            EmptyScreen(modifier = modifier)
        }
    }
}
