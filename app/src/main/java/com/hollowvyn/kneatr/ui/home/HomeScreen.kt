package com.hollowvyn.kneatr.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hollowvyn.kneatr.R
import com.hollowvyn.kneatr.domain.fakes.ContactFakes
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.ui.components.screenstates.ErrorScreen
import com.hollowvyn.kneatr.ui.helpers.getRelativeDateString

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(modifier = modifier) { innerPadding ->
        when (uiState) {
            is HomeUiState.Success -> {
                LazyColumn {
                    items((uiState as HomeUiState.Success).randomContacts) {
                        ContactCard(it) {
                            viewModel.addCommunicationLog(it.id)
                        }
                    }
                }
            }

            is HomeUiState.Error -> {
                ErrorScreen {
                }
            }

            is HomeUiState.Loading -> {
            }

            else -> {}
        }
    }
}

@Composable
fun ContactCard(
    contact: Contact,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onMarkAsComplete: () -> Unit = {},
) {
    ListItem(
        modifier = modifier.clickable { onClick() },
        headlineContent = { Text(text = contact.name) },
        supportingContent = {
            contact.lastCommunicationDateRelative?.getRelativeDateString()?.let {
                val text = stringResource(R.string.last_time_contacted, it)
                Text(text = text, maxLines = 2)
            }
        },
        trailingContent = {
            TextButton(
                onClick = onMarkAsComplete,
                content = {
                    Text(
                        text = "Mark as Complete",
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
            )
        },
    )
}

@Preview
@Composable
private fun ContactCardPreview() {
    ContactCard(
        contact = ContactFakes.contactWithLogs,
    )
}

sealed interface HomeUiState {
    data class Success(
        val overdueContacts: List<Contact>,
        val randomContacts: List<Contact>,
        val upcomingContacts: List<Contact>,
        val contactDueToday: List<Contact>,
    ) : HomeUiState

    object Error : HomeUiState

    object Loading : HomeUiState
}
