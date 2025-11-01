
package com.hollowvyn.kneatr.ui.contact.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.domain.model.ContactTier
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactsList(
    grouped: Map<String, List<Contact>>,
    modifier: Modifier = Modifier,
    onContactClick: (Contact) -> Unit = {},
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val groupIndexes = mutableMapOf<String, Int>()
    var currentIndex = 0
    grouped.forEach {
        groupIndexes[it.key] = currentIndex
        currentIndex += it.value.size + 1
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
    ) {
        grouped.forEach { (initial, contactsForInitial) ->
            stickyHeader {
                CharacterHeader(initial) {
                    coroutineScope.launch {
                        groupIndexes[initial]?.let { index ->
                            listState.animateScrollToItem(index)
                        }
                    }
                }
            }

            contactsItems(contactsForInitial) {
                onContactClick(it)
            }
        }
    }
}

fun LazyListScope.contactsItems(
    contacts: List<Contact>,
    onContactClick: (Contact) -> Unit = {},
) {
    items(
        items = contacts,
        key = { it.id },
        contentType = Contact::name,
    ) { contact ->
        ContactListItem(
            name = contact.name,
            phoneNumber = contact.phoneNumber,
            tier = contact.tier,
            onClick = { onContactClick(contact) },
        )
        if (contacts.last() != contact) {
            HorizontalDivider()
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
    ContactsList(
        grouped = contacts.groupBy { it.name[0].uppercase() },
        modifier = Modifier.fillMaxSize(),
    )
}
