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
import com.hollowvyn.kneatr.domain.model.ContactDto
import com.hollowvyn.kneatr.domain.model.ContactTierDto

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
