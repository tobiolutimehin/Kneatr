package com.hollowvyn.kneatr.ui.contact

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hollowvyn.kneatr.ui.components.screenstates.EmptyScreen
import com.hollowvyn.kneatr.ui.components.screenstates.ErrorScreen
import com.hollowvyn.kneatr.ui.components.screenstates.LoadingScreen

@Composable
fun ContactsListScreen(
    modifier: Modifier = Modifier,
    viewModel: ContactsListViewModel = hiltViewModel<ContactsListViewModel>(),
) {
    val uiStateDelegate by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiStateDelegate) {
        is ContactsListUiState.Success -> {
            val uiState = uiStateDelegate as ContactsListUiState.Success

            ContactsListSuccessContent(
                contacts = uiState.contacts,
                modifier = modifier,
                onContactClick = {},
                searchedContacts = uiState.searchedContacts,
                query = uiState.query,
                onQueryChange = viewModel::onQueryChange,
            )
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
