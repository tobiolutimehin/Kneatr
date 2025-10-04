package com.hollowvyn.kneatr.ui.contact

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.ui.contact.viewmodel.ContactDetailViewModel

sealed class ContactDetailUiState {
    data class Success(
        val contact: Contact?,
    ) : ContactDetailUiState()

    data object Error : ContactDetailUiState()

    data object Loading : ContactDetailUiState()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailScreen(
    contactId: Long,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ContactDetailViewModel = hiltViewModel<ContactDetailViewModel>(),
) {
    LaunchedEffect(key1 = contactId) {
        viewModel.loadContactId(contactId)
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    val title =
                        (uiState as? ContactDetailUiState.Success)?.contact?.name
                            ?: "" // Or a default title
                    Text(text = title)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "stringResource(id = R.string.navigate_back)",
                        )
                    }
                },
            )
        },
        bottomBar = {
            Button(
                onClick = { /*TODO*/ },
                modifier =
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
            ) {
                Text("Mark as completed")
            }
        },
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)

        when (val state = uiState) {
            is ContactDetailUiState.Success -> {
                state.contact?.let {
                    ContactDetailContent(
                        contact = it,
                        modifier = contentModifier,
                    )
                } ?: Text("Contact not found", modifier = contentModifier) // Handle null contact
            }

            ContactDetailUiState.Error -> Text("Error loading contact", modifier = contentModifier)
            ContactDetailUiState.Loading -> Text("Loading...", modifier = contentModifier)
        }
    }
}

@Composable
private fun ContactDetailContent(
    contact: Contact,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
    ) {
        item {
            Text(text = "Name: ${contact.name}")
        }
        item {
            if (contact.tags.isNotEmpty()) {
                Text(text = "Tags: ${contact.tags.joinToString(", ") { it.name }}")
            }
        }
        item {
            Row {
                Text("last time contacted: ${contact.communicationLogs.lastOrNull()?.date ?: "Never"}")
                Text("next contact in: ${contact.customFrequencyDays ?: "Never"}")
            }
        }
        item {
            contact.tier?.let { Text(text = "Tier: ${contact.tier.name}") }
        }

        item {
            Text(text = "Phone: ${contact.phoneNumber}")
            contact.email?.let { email ->
                Text(text = "Email: $email")
            }
        }
    }
}
