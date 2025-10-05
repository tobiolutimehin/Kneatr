package com.hollowvyn.kneatr.ui.contact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.domain.model.ContactTier

@Composable
fun ContactsList(
    contacts: List<Contact>,
    modifier: Modifier = Modifier,
    onContactClick: (Contact) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        contentPadding = PaddingValues(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        itemsIndexed(
            items = contacts,
            key = { _, contact -> contact.id },
            contentType = { _, _ -> "ContactListItem" },
        ) { idx, contact ->
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
private fun ContactsListPreview() {
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
    ContactsList(contacts = contacts, modifier = Modifier.fillMaxSize())
}
