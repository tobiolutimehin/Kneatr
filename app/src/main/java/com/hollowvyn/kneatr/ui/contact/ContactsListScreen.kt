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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when (uiState) {
        is ContactsListUiState.Success -> {
            val list = (uiState as ContactsListUiState.Success).contacts
            ContactsListSuccessContent(
                contacts = list,
                modifier = modifier,
                onContactClick = {
                    // navigate to contact details
                },
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
