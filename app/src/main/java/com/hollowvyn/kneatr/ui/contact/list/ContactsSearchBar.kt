package com.hollowvyn.kneatr.ui.contact.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.hollowvyn.kneatr.R
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.domain.model.ContactTier
import com.hollowvyn.kneatr.ui.theme.KneatrTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsSearchBar(
    query: String,
    searchedContacts: List<Contact>,
    onQueryChange: (String) -> Unit,
    onContactClick: (Contact) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var isInitialComposition by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (isInitialComposition && expanded) {
            focusManager.clearFocus()
            expanded = false
            isInitialComposition = false
        }
    }

    SearchBar(
        modifier = modifier.fillMaxWidth(),
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = {
                    expanded = false
                    focusManager.clearFocus()
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text(text = stringResource(id = R.string.contact_list_search_hint)) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    Icon(
                        Icons.Default.Close,
                        contentDescription =
                            stringResource(
                                if (query.isEmpty() && expanded) {
                                    R.string.contact_list_close_search_icon_description
                                } else {
                                    R.string.contact_list_clear_search_icon_description
                                },
                            ),
                        modifier =
                            Modifier.clickable(
                                enabled = expanded || query.isNotEmpty(),
                            ) {
                                if (query.isEmpty()) {
                                    expanded = false
                                    focusManager.clearFocus()
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
                focusManager.clearFocus()
                onQueryChange("")
                onContactClick(it)
            },
            modifier =
                Modifier
                    .fillMaxSize()
                    .clickable {
                    expanded = false
                    focusManager.clearFocus()
                },
                )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun ContactsSearchBarPreview() {
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
    KneatrTheme {
        ContactsSearchBar(
            query = "",
            searchedContacts = contacts,
            onQueryChange = {},
            onContactClick = {},
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
        )
    }
}
