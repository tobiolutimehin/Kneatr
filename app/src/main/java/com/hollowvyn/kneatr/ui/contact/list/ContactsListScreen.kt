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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hollowvyn.kneatr.domain.fakes.ContactFakes.allContacts
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.ui.components.screenstates.EmptyScreen
import com.hollowvyn.kneatr.ui.components.screenstates.ErrorScreen
import com.hollowvyn.kneatr.ui.components.screenstates.LoadingScreen
import com.hollowvyn.kneatr.ui.contact.viewmodel.ContactsListUiState
import com.hollowvyn.kneatr.ui.contact.viewmodel.ContactsListViewModel
import com.hollowvyn.kneatr.ui.theme.KneatrTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsListScreen(
    modifier: Modifier = Modifier,
    viewModel: ContactsListViewModel = hiltViewModel(),
    onContactClick: (Contact) -> Unit = {},
) {
    val uiStateDelegate by viewModel.uiState.collectAsStateWithLifecycle()

    ContactsListScreen(
        uiState = uiStateDelegate,
        modifier = modifier,
        onContactClick = onContactClick,
        onQueryChange = viewModel::onQueryChange,
    )
}

@Composable
private fun ContactsListScreen(
    uiState: ContactsListUiState,
    modifier: Modifier = Modifier,
    onContactClick: (Contact) -> Unit = {},
    onQueryChange: (String) -> Unit = {},
) {
    Scaffold(modifier = modifier) { innerPadding ->
        val contentModifier =
            Modifier
                .padding(innerPadding)
                .consumeWindowInsets(WindowInsets.statusBars)
        when (uiState) {
            is ContactsListUiState.Success -> {
                uiState.let { success ->
                    ContactsListSuccessContent(
                        contacts = success.contacts,
                        modifier = contentModifier,
                        onContactClick = onContactClick,
                        searchedContacts = success.searchedContacts,
                        query = success.query,
                        onQueryChange = onQueryChange,
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

class UserPreviewParameterProvider : PreviewParameterProvider<ContactsListUiState> {
    override val values =
        sequenceOf(
            ContactsListUiState.Success(
                contacts = allContacts,
                searchedContacts = emptyList(),
                query = "",
            ),
            ContactsListUiState.Error,
            ContactsListUiState.Loading,
            ContactsListUiState.Empty,
        )
}

@Preview(showSystemUi = true)
@Composable
private fun ContactsListsScreenPreview(
    @PreviewParameter(UserPreviewParameterProvider::class) uiState: ContactsListUiState,
) {
    KneatrTheme {
        ContactsListScreen(
            uiState = uiState,
        )
    }
}
