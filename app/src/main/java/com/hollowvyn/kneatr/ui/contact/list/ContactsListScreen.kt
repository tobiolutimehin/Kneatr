package com.hollowvyn.kneatr.ui.contact.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hollowvyn.kneatr.KneatrApplication
import com.hollowvyn.kneatr.domain.fakes.ContactFakes.allContacts
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.domain.model.ContactTag
import com.hollowvyn.kneatr.domain.model.ContactTier
import com.hollowvyn.kneatr.ui.components.screenstates.EmptyScreen
import com.hollowvyn.kneatr.ui.components.screenstates.ErrorScreen
import com.hollowvyn.kneatr.ui.components.screenstates.LoadingScreen
import com.hollowvyn.kneatr.ui.contact.list.viewmodel.ContactsListUiState
import com.hollowvyn.kneatr.ui.contact.list.viewmodel.ContactsListViewModel
import com.hollowvyn.kneatr.ui.theme.KneatrTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsListScreen(
    modifier: Modifier = Modifier,
    viewModel: ContactsListViewModel = hiltViewModel(),
    onContactClick: (Contact) -> Unit = {},
) {
    val uiStateDelegate by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val application = context.applicationContext as KneatrApplication

    ContactsListScreen(
        uiState = uiStateDelegate,
        modifier = modifier,
        onContactClick = onContactClick,
        onQueryChange = viewModel::onQueryChange,
        onReSyncContacts = {
            application.triggerImmediateContactSync(context)
        },
    )
}

@Composable
private fun ContactsListScreen(
    uiState: ContactsListUiState,
    modifier: Modifier = Modifier,
    onContactClick: (Contact) -> Unit = {},
    onQueryChange: (String) -> Unit = {},
    onReSyncContacts: () -> Unit = {},
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
                        groupedContacts = success.groupedContacts,
                        modifier = contentModifier,
                        onContactClick = onContactClick,
                        searchedContacts = success.searchedContacts,
                        query = success.query,
                        onQueryChange = onQueryChange,
                        onReSyncContacts = onReSyncContacts,
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

@Composable
fun ContactsListFilter(
    tags: List<ContactTag>,
    tiers: List<ContactTier>,
    selectedTags: List<ContactTag>,
    selectedTiers: List<ContactTier>,
    overdueSelected: Boolean,
    dueTodaySelected: Boolean,
    onSelectDueToday: (Boolean) -> Unit,
    onSelectOverDue: (Boolean) -> Unit,
    onSelectTag: (ContactTag) -> Unit,
    onSelectTier: (ContactTier) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .wrapContentHeight()
                .fillMaxWidth(),
    ) {
        Text("Contact Filters")
        Text("Tier:")
        Text("Tags(Searchable):")
        Text("Due state:")
        Text("Sort by:")
    }
}

@Preview
@Composable
private fun ContactsListFilterPreview() {
    KneatrTheme {
        ContactsListFilter(
            tags = listOf(ContactTag(name = "Family"), ContactTag(name = "Work")),
            tiers =
                listOf(
                    ContactTier(id = 1, name = "Tier 1", daysBetweenContact = 7),
                    ContactTier(id = 2, name = "Tier 2", daysBetweenContact = 30),
                ),
            selectedTags = listOf(ContactTag(name = "Family")),
            selectedTiers = listOf(ContactTier(id = 1, name = "Tier 1", daysBetweenContact = 7)),
            overdueSelected = false,
            dueTodaySelected = true,
            onSelectDueToday = {},
            onSelectOverDue = {},
            onSelectTag = {},
            onSelectTier = {},
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                )
    }
}
