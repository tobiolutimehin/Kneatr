package com.hollowvyn.kneatr.ui.contact.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hollowvyn.kneatr.domain.model.CommunicationLog
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.domain.util.formatPhoneNumber
import com.hollowvyn.kneatr.ui.contact.DeepInteractionConfirmationDialog
import com.hollowvyn.kneatr.ui.contact.viewmodel.ContactDetailViewModel
import com.hollowvyn.kneatr.ui.util.startEmail
import com.hollowvyn.kneatr.ui.util.startPhoneCall
import com.hollowvyn.kneatr.ui.util.startTextMessage

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
    viewModel: ContactDetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = contactId) {
        viewModel.loadContactId(contactId)
    }

    val uiState by viewModel.uiState.collectAsState()

    val listState = rememberLazyListState()
    val isScrolled = remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedLog by remember { mutableStateOf<CommunicationLog?>(null) }

    var showConfirmationDialog by remember { mutableStateOf(false) }
    var confirmationAction by remember { mutableStateOf<() -> Unit>({}) }
    var confirmationTitle by remember { mutableStateOf("") }
    var confirmationText by remember { mutableStateOf("") }

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
                onClick = {
                    selectedLog = null // Ensure we're adding a new log
                    showBottomSheet = true
                },
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
                        onEditLog = { log ->
                            selectedLog = log
                            showBottomSheet = true
                        },
                        onDeleteLog = { log ->
                            viewModel.deleteCommunicationLog(log)
                        },
                        onShowConfirmation = { title, text, action ->
                            confirmationTitle = title
                            confirmationText = text
                            confirmationAction = action
                            showConfirmationDialog = true
                        }
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

        if (showBottomSheet) {
            CommunicationLogBottomSheet(
                logToEdit = selectedLog,
                onSave = { id, date, type, notes ->
                    if (id == null) {
                        viewModel.addCommunicationLog(date, type, notes)
                    } else {
                        viewModel.updateCommunicationLog(date, type, notes, id)
                    }
                },
                dismissBottomSheet = { showBottomSheet = false },
            )
        }

        if (showConfirmationDialog) {
            DeepInteractionConfirmationDialog(
                onConfirm = confirmationAction,
                onDismiss = { showConfirmationDialog = false },
                title = confirmationTitle,
                text = confirmationText
            )
        }
    }
}

@Composable
private fun ContactDetailContent(
    contact: Contact,
    listState: LazyListState,
    onEditLog: (CommunicationLog) -> Unit,
    onDeleteLog: (CommunicationLog) -> Unit,
    onShowConfirmation: (String, String, () -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
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
        item { ContactDateInfo(contact) }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                if (contact.phoneNumber.isNotBlank()) {
                    Button(
                        onClick = {
                            onShowConfirmation(
                                "Call Contact",
                                "Are you sure you want to call ${contact.name}?"
                            ) { context.startPhoneCall(contact.phoneNumber) }
                        }
                    ) {
                        Text("Call")
                    }

                    Button(
                        onClick = {
                            onShowConfirmation(
                                "Message Contact",
                                "Are you sure you want to message ${contact.name}?"
                            ) { context.startTextMessage(contact.phoneNumber) }
                        }
                    ) {
                        Text("Message")
                    }
                }

                contact.email?.let { email ->
                    Button(
                        onClick = {
                            onShowConfirmation(
                                "Email Contact",
                                "Are you sure you want to email ${contact.name}?"
                            ) { context.startEmail(email) }
                        }
                    ) {
                        Text("Email")
                    }
                }
            }
        }
        item {
            contact.tier?.let { Text(text = "Tier: ${contact.tier.name}") }
        }

        item {
            Text(
                text = "Phone: ${contact.phoneNumber.formatPhoneNumber()}",
            )
            contact.email?.let { email ->
                Text(text = "Email: $email")
            }
        }

        communicationLogItems(
            communicationLog = contact.communicationLogs,
            onEditLog = { onEditLog(it) },
            onDeleteLog = { onDeleteLog(it) },
        )
    }
}
