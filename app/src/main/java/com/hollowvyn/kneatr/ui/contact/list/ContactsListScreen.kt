package com.hollowvyn.kneatr.ui.contact.list

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.ui.components.screenstates.EmptyScreen
import com.hollowvyn.kneatr.ui.components.screenstates.ErrorScreen
import com.hollowvyn.kneatr.ui.components.screenstates.LoadingScreen
import com.hollowvyn.kneatr.ui.contact.viewmodel.ContactsListUiState
import com.hollowvyn.kneatr.ui.contact.viewmodel.ContactsListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsListScreen(
    modifier: Modifier = Modifier,
    viewModel: ContactsListViewModel = hiltViewModel(),
    onContactClick: (Contact) -> Unit = {},
) {
    val uiStateDelegate by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        val contentModifier =
            Modifier
                .padding(innerPadding)
                .consumeWindowInsets(WindowInsets.statusBars)
        when (uiStateDelegate) {
            is ContactsListUiState.Success -> {
                (uiStateDelegate as ContactsListUiState.Success).let { success ->
                    ContactsListSuccessContent(
                        contacts = success.contacts,
                        modifier = contentModifier,
                        onContactClick = onContactClick,
                        searchedContacts = success.searchedContacts,
                        query = success.query,
                        onQueryChange = viewModel::onQueryChange,
                    )
                }
            }

            is ContactsListUiState.Error -> {
                ErrorScreen(modifier = contentModifier)
            }

            is ContactsListUiState.Loading -> {
                LoadingScreen(modifier = contentModifier)
            }

            is ContactsListUiState.Empty -> {
                EmptyScreen(modifier = contentModifier)
            }
        }
    }
}
