package com.hollowvyn.kneatr.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                    text = "Reached out today!",
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
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
    ) {
        homeScreenContactSections(
            section = HomeScreenSection.Overdue,
            state = state,
            openContact = openContact,
            markAsComplete = markAsComplete,
        )
        homeScreenContactSections(
            section = HomeScreenSection.Random,
            state = state,
            openContact = openContact,
            markAsComplete = markAsComplete,
        )

        homeScreenContactSections(
            section = HomeScreenSection.Upcoming,
            state = state,
            openContact = openContact,
            markAsComplete = markAsComplete,
        )
        homeScreenContactSections(
            section = HomeScreenSection.DueToday,
            state = state,
            openContact = openContact,
            markAsComplete = markAsComplete,
        )
    }
}

fun LazyListScope.homeScreenContactSections(
    section: HomeScreenSection,
    state: HomeUiState.Success,
    openContact: (Contact) -> Unit,
    markAsComplete: (Contact) -> Unit,
) {
    item {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = section.title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )

            section.onRefresh?.let {
                IconButton(
                    onClick = it,
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
            }
        }
    }

    val contacts = section.filter.invoke(state)
    if (!contacts.isEmpty()) {
        items(contacts) {
            ContactCard(
                it,
                onClick = { openContact(it) },
                onMarkAsComplete = { markAsComplete(it) },
            )
        }
    } else {
        item {
            Text(
                text = "No ${section.title} for now",
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
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
                randomContacts = ContactFakes.allContacts.take(4),
                upcomingContacts = ContactFakes.allContacts.take(2),
                contactDueToday = ContactFakes.allContacts.take(3),
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

enum class HomeScreenSection(
    val title: String,
    val filter: (HomeUiState.Success) -> List<Contact>,
    val onRefresh: (() -> Unit)? = null,
) {
    Overdue(
        title = "Overdue",
        filter = { it.overdueContacts },
    ),
    Random(
        title = "Random",
        filter = { it.randomContacts },
        onRefresh = { /*TODO*/ },
    ),
    Upcoming(
        title = "Upcoming",
        filter = { it.upcomingContacts },
    ),
    DueToday(
        title = "Contacts Due Today",
        filter = { it.contactDueToday },
    );
}