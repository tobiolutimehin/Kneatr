package com.hollowvyn.kneatr.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hollowvyn.kneatr.R
import com.hollowvyn.kneatr.domain.fakes.ContactFakes
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.ui.home.viewmodel.HomeScreenSection
import com.hollowvyn.kneatr.ui.home.viewmodel.HomeUiState

@Composable
internal fun HomeSuccessContent(
    state: HomeUiState.Success,
    openContact: (Contact) -> Unit,
    markAsComplete: (Contact) -> Unit,
    modifier: Modifier = Modifier,
    onSectionRefresh: ((HomeScreenSection) -> Unit)? = null,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
    ) {
        HomeScreenSection.entries.forEach { section ->
            homeScreenContactSections(
                section = section,
                state = state,
                openContact = openContact,
                markAsComplete = markAsComplete,
                onSectionRefresh = onSectionRefresh,
            )
        }
    }
}

private fun LazyListScope.homeScreenContactSections(
    section: HomeScreenSection,
    state: HomeUiState.Success,
    openContact: (Contact) -> Unit,
    markAsComplete: (Contact) -> Unit,
    onSectionRefresh: ((HomeScreenSection) -> Unit)? = null,
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

            if (section.onRefresh != null) {
                // Use the debounced click handler here
                val debouncedRefresh =
                    rememberDebouncedOnClick {
                        onSectionRefresh?.invoke(section)
                    }

                IconButton(
                    onClick = debouncedRefresh,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.refresh_24px),
                        contentDescription = "Refresh ${section.title}",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
            }
        }
    }

    val contacts = section.filter(state)
    if (contacts.isNotEmpty()) {
        items(
            contacts,
            key = { it.id },
            contentType = { it::class.java },
        ) {
            HomeContactCard(
                it,
                onClick = { openContact(it) },
                onMarkAsComplete = { markAsComplete(it) },
                modifier = Modifier.animateItem(),
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
                        .padding(20.dp)
                        .animateItem(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeSuccessContentPreview() {
    HomeSuccessContent(
        state =
            HomeUiState.Success(
                overdueContacts = ContactFakes.allContacts.take(2),
                randomContacts = emptyList(),
                upcomingContacts = emptyList(),
                contactDueToday = emptyList(),
            ),
        openContact = {},
        markAsComplete = {},
    )
}
