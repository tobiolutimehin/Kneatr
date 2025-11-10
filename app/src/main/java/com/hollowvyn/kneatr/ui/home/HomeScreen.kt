package com.hollowvyn.kneatr.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.ui.components.screenstates.ErrorScreen
import com.hollowvyn.kneatr.ui.components.screenstates.LoadingScreen
import com.hollowvyn.kneatr.ui.home.viewmodel.HomeUiState
import com.hollowvyn.kneatr.ui.home.viewmodel.HomeViewModel

@Composable
internal fun HomeScreen(
    openContact: (Contact) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(modifier = modifier) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)
        when (uiState) {
            is HomeUiState.Success -> {
                HomeSuccessContent(
                    state = uiState as HomeUiState.Success,
                    openContact = openContact,
                    markAsComplete = { viewModel.markContactAsCompleted(it.id) },
                    modifier = contentModifier,
                )
            }

            is HomeUiState.Error -> {
                ErrorScreen(modifier = contentModifier)
            }

            is HomeUiState.Loading -> {
                LoadingScreen(modifier = contentModifier)
            }
        }
    }
}
