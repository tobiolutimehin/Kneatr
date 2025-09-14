package com.hollowvyn.kneatr.ui.contact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
    Column(
        modifier = modifier.padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ContactsSearchBar(
            query = query,
            onQueryChange = onQueryChange,
            searchedContacts = searchedContacts,
            onContactClick = onContactClick,
        )

        // todo Mini filter for overdue contacts, find by pills, etc. lazy row for options

        ContactsList(
            contacts = contacts,
            onContactClick = onContactClick,
        )
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
