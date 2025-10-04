package com.hollowvyn.kneatr.ui.contact

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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

    val listState = rememberLazyListState()
    val isScrolled = remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    val title =
                        (uiState as? ContactDetailUiState.Success)?.contact?.name
                            ?: "" // Or a default title
                    AnimatedVisibility(
                        visible = isScrolled.value,
                        enter = fadeIn(spring(stiffness = Spring.StiffnessLow)),
                        exit = fadeOut(spring(stiffness = Spring.StiffnessLow)),
                    ) { Text(text = title) }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back",
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add log") },
                onClick = { /*TODO*/ },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add communication log",
                    )
                },
            )
        },
    ) { innerPadding ->
        when (val state = uiState) {
            is ContactDetailUiState.Success -> {
                state.contact?.let {
                    ContactDetailContent(
                        contact = it,
                        modifier = Modifier.padding(innerPadding),
                        listState = listState,
                        isScrolled = isScrolled.value,
                    )
                } ?: Text("Contact not found", modifier = Modifier.padding(innerPadding))
            }

            ContactDetailUiState.Error ->
                Text(
                    "Error loading contact",
                    modifier = Modifier.padding(innerPadding),
                )

            ContactDetailUiState.Loading ->
                Text(
                    "Loading...",
                    modifier = Modifier.padding(innerPadding),
                )
        }
    }
}

@Composable
private fun ContactDetailContent(
    contact: Contact,
    listState: LazyListState,
    isScrolled: Boolean,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        state = listState,
    ) {
        item {
            Text(
                text = contact.name,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(vertical = 16.dp),
            )
        }
        item {
            if (contact.tags.isNotEmpty()) {
                Text(text = "Tags: ${contact.tags.joinToString(", ") { it.name }}")
            }
        }
        item {
            Row {
                Text("last time contacted: ${contact.lastDate ?: "Never"}")
                Text("next contact in: ${contact.nextContactDate ?: "Never"}")
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

        items(100) {
            Text(text = "Log $it")
        }
    }
}
