package com.hollowvyn.kneatr.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
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
    openContact: (Contact) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(modifier = modifier) { innerPadding ->
        when (uiState) {
            is HomeUiState.Success -> {
                HomeSuccessContent(
                    state = uiState as HomeUiState.Success,
                    openContact = openContact,
                    markAsComplete = { viewModel.addCommunicationLog(it.id) },
                    modifier = Modifier.padding(innerPadding),
                )
            }

            is HomeUiState.Error -> {
                ErrorScreen {
                }
            }

            is HomeUiState.Loading -> {
            }
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
            if (contact.reachedOutToday) {
                Text(
                    text = "Reached out tody!",
                    style = MaterialTheme.typography.labelSmall,
                )
            } else {
                TextButton(
                    onClick = onMarkAsComplete,
                    content = {
                        Text(
                            text = "Mark as Complete",
                            style = MaterialTheme.typography.labelSmall,
                        )
                    },
                )
            }
        },
    )
}

@Composable
fun HomeSuccessContent(
    state: HomeUiState.Success,
    openContact: (Contact) -> Unit,
    markAsComplete: (Contact) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            Text("Reach out to friends")
        }
        items(state.randomContacts) {
            ContactCard(
                it,
                onClick = { openContact(it) },
                onMarkAsComplete = { markAsComplete(it) },
            )
        }

        item {
            Text("Reach out to them today!")
        }
        items(state.contactDueToday) {
            ContactCard(
                it,
                onClick = { openContact(it) },
                onMarkAsComplete = { markAsComplete(it) },
            )
        }

        item {
            Text("Uh oh! These are overdue")
        }
        items(state.overdueContacts) {
            ContactCard(
                it,
                onClick = { openContact(it) },
                onMarkAsComplete = { markAsComplete(it) },
            )
        }

        item {
            Text("These are coming up")
        }
        items(state.upcomingContacts) {
            ContactCard(
                it,
                onClick = { openContact(it) },
                onMarkAsComplete = { markAsComplete(it) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeSuccessContentPreview() {
    HomeSuccessContent(
        state =
            HomeUiState.Success(
                overdueContacts = ContactFakes.allContacts.take(5),
                randomContacts = ContactFakes.allContacts.take(5),
                upcomingContacts = ContactFakes.allContacts.take(5),
                contactDueToday = ContactFakes.allContacts.take(5),
            ),
        openContact = {},
        markAsComplete = {},
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
