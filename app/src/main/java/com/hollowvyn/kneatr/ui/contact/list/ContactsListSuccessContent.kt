package com.hollowvyn.kneatr.ui.contact.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.domain.model.ContactTier

@Composable
fun ContactsListSuccessContent(
    groupedContacts: Map<String, List<Contact>>,
    searchedContacts: List<Contact>,
    query: String,
    modifier: Modifier = Modifier,
    onContactClick: (Contact) -> Unit = {},
    onQueryChange: (String) -> Unit = {},
    onReSyncContacts: () -> Unit = {},
) {
    Column(
        modifier = modifier.padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row {
            ContactsSearchBar(
                query = query,
                onQueryChange = onQueryChange,
                searchedContacts = searchedContacts,
                onContactClick = onContactClick,
                modifier = Modifier.weight(1f),
            )

            IconButton(
                onClick = onReSyncContacts,
            ) {
                Icon(
                    imageVector = Icons.Filled.Abc,
                    contentDescription = "Sort by name",
                )
            }
        }

        // todo Mini filter for overdue contacts, find by pills, etc. lazy row for options

        ContactsList(
            grouped = groupedContacts,
            onContactClick = onContactClick,
        )
    }
}

@Preview
@Composable
private fun ContactsListSuccessContentPreview() {
    val contacts =
        listOf(
            Contact(
                id = 1,
                name = "John Doe",
                phoneNumber = "123-456-7890",
                email = "john.doe@example.com",
                tier = ContactTier(id = 1, name = "Tier 1", daysBetweenContact = 7),
            ),
            Contact(
                id = 2,
                name = "Jane Smith",
                phoneNumber = "987-654-3210",
                email = "jane.smith@example.com",
                tier = ContactTier(id = 2, name = "Tier 2", daysBetweenContact = 14),
            ),
        )
    ContactsListSuccessContent(
        groupedContacts = contacts.groupBy { it.name[0].uppercase() },
        searchedContacts = contacts,
        query = "",
    )
}
