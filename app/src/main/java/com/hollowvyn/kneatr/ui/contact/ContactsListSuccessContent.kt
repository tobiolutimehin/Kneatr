package com.hollowvyn.kneatr.ui.contact

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hollowvyn.kneatr.domain.model.ContactDto
import com.hollowvyn.kneatr.domain.model.ContactTierDto

@Composable
fun ContactsListSuccessContent(
    contacts: List<ContactDto>,
    searchedContacts: List<ContactDto>,
    query: String,
    modifier: Modifier = Modifier,
    onContactClick: (ContactDto) -> Unit = {},
    onQueryChange: (String) -> Unit = {},
) {
    Column(modifier = modifier) {
        ContactsSearchBar(
            query = query,
            onQueryChange = onQueryChange,
            searchedContacts = searchedContacts,
            onContactClick = onContactClick,
        )
        ContactsList(
            contacts = contacts,
            onContactClick = onContactClick,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContactsSearchBar(
    query: String,
    searchedContacts: List<ContactDto>,
    onQueryChange: (String) -> Unit,
    onContactClick: (ContactDto) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    SearchBar(
        modifier = modifier.fillMaxWidth(),
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = { expanded = false },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text("Search for contacts") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null,
                        modifier =
                            Modifier.clickable {
                                if (query.isEmpty()) {
                                    expanded = false
                                } else {
                                    onQueryChange("")
                                }
                            },
                    )
                },
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        ContactsList(
            contacts = searchedContacts,
            onContactClick = {
                expanded = false
                onQueryChange("")
                onContactClick(it)
            },
            modifier =
                Modifier
                    .fillMaxSize()
                    .clickable {
                        expanded = false
                    },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ContactsSearchBarPreview() {
    val contacts =
        listOf(
            ContactDto(
                id = 1,
                name = "John Doe",
                phoneNumber = "123-456-7890",
                tier = ContactTierDto(id = 1, name = "Tier 1", daysBetweenContact = 7),
                email = "john.doe@example.com",
            ),
            ContactDto(
                id = 2,
                name = "Jane Smith",
                phoneNumber = "987-654-3210",
                tier = ContactTierDto(id = 2, name = "Tier 2", daysBetweenContact = 14),
                email = "jane.smith@example.com",
            ),
        )
    ContactsSearchBar(
        query = "",
        searchedContacts = contacts,
        onQueryChange = {},
        onContactClick = {},
    )
}

@Composable
fun ContactsList(
    contacts: List<ContactDto>,
    modifier: Modifier = Modifier,
    onContactClick: (ContactDto) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        contentPadding = PaddingValues(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        itemsIndexed(contacts) { idx, contact ->
            ContactListItem(
                name = contact.name,
                phoneNumber = contact.phoneNumber,
                tier = contact.tier,
                onClick = { onContactClick(contact) },
            )
            if (idx < contacts.lastIndex && contacts.size > 1) {
                HorizontalDivider()
            }
        }
    }
}

@Preview
@Composable
private fun ContactsListSuccessContentPreview() {
    val contacts =
        listOf(
            ContactDto(
                id = 1,
                name = "John Doe",
                phoneNumber = "123-456-7890",
                tier = ContactTierDto(id = 1, name = "Tier 1", daysBetweenContact = 7),
                email = "john.doe@example.com",
            ),
            ContactDto(
                id = 2,
                name = "Jane Smith",
                phoneNumber = "987-654-3210",
                tier = ContactTierDto(id = 2, name = "Tier 2", daysBetweenContact = 14),
                email = "jane.smith@example.com",
            ),
        )
    ContactsListSuccessContent(
        contacts = contacts,
        searchedContacts = contacts,
        query = "",
    )
}

@Preview
@Composable
private fun ContactsListPreview() {
    val contacts =
        listOf(
            ContactDto(
                id = 1,
                name = "John Doe",
                phoneNumber = "123-456-7890",
                tier = ContactTierDto(id = 1, name = "Tier 1", daysBetweenContact = 7),
                email = "john.doe@example.com",
            ),
            ContactDto(
                id = 2,
                name = "Jane Smith",
                phoneNumber = "987-654-3210",
                tier = ContactTierDto(id = 2, name = "Tier 2", daysBetweenContact = 14),
                email = "jane.smith@example.com",
            ),
        )
    ContactsList(contacts = contacts, modifier = Modifier.fillMaxSize())
}
