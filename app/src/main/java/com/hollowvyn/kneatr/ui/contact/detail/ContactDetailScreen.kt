package com.hollowvyn.kneatr.ui.contact.detail

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hollowvyn.kneatr.R
import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import com.hollowvyn.kneatr.domain.model.CommunicationLog
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.domain.model.ContactTag
import com.hollowvyn.kneatr.domain.model.ContactTier
import com.hollowvyn.kneatr.domain.util.DateTimeUtils
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
    var confirmationAction by remember { mutableStateOf({}) }
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
                        onShowConfirmation = { text, type, action ->
                            confirmationText = text
                            confirmationAction = {
                                action()
                                viewModel.addCommunicationLog(DateTimeUtils.today(), type)
                            }
                            showConfirmationDialog = true
                        },
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
                text = confirmationText,
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
    onShowConfirmation: (String, CommunicationType, () -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Text(
                text = contact.name,
                style = MaterialTheme.typography.headlineLarge,
            )
        }
        item {
            contact.tier?.let { Text(text = "Tier: ${contact.tier.name}") }
        }
        item {
            if (contact.tags.isNotEmpty()) {
                Text(text = "Tags: ${contact.tags.joinToString(", ") { it.name }}")
            }
        }
        item { ContactDateInfo(contact) }

        item {
            ContactReachOutButtons(
                contact = contact,
                onShowConfirmation = onShowConfirmation,
            )
        }

        communicationLogItems(
            communicationLog = contact.communicationLogs,
            onEditLog = { onEditLog(it) },
            onDeleteLog = { onDeleteLog(it) },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ContactDetailContentPreview() {
    val contact =
        Contact(
            id = 1,
            name = "John Doe",
            phoneNumber = "1234567890",
            email = "john.doe@email.com",
            tags =
                setOf(
                    ContactTag(id = 1, name = "Family"),
                    ContactTag(id = 2, name = "Work"),
                ),
            tier = ContactTier(id = 1, name = "Tier 1", daysBetweenContact = 30),
        )
    ContactDetailContent(
        contact = contact,
        listState = rememberLazyListState(),
        onEditLog = {},
        onDeleteLog = {},
        onShowConfirmation = { _, _, _ ->
        },
        modifier = Modifier,
    )
}

@Composable
fun ContactReachOutButtons(
    contact: Contact,
    onShowConfirmation: (String, CommunicationType, () -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Row(
        modifier = modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
    ) {
        if (contact.phoneNumber.isNotBlank()) {
            ContactReachOutButton(
                text = context.getString(R.string.call),
                icon = R.drawable.call_24px,
                onClick = {
                    onShowConfirmation(
                        context.getString(R.string.call_x_contact, contact.name),
                        CommunicationType.PHONE_CALL,
                    ) {
                        context.startPhoneCall(contact.phoneNumber)
                    }
                },
            )

            ContactReachOutButton(
                text = context.getString(R.string.message),
                icon = R.drawable.chat_bubble_24px,
                onClick = {
                    onShowConfirmation(
                        context.getString(R.string.message_x_contact, contact.name),
                        CommunicationType.MESSAGE,
                    ) {
                        context.startTextMessage(contact.phoneNumber)
                    }
                },
            )
        }

        contact.email?.let { email ->
            ContactReachOutButton(
                text = context.getString(R.string.email),
                icon = R.drawable.mail_24px,
                onClick = {
                    onShowConfirmation(
                        context.getString(R.string.email_x_contact, contact.name),
                        CommunicationType.EMAIL,
                    ) {
                        context.startEmail(email)
                    }
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ContactReachOutButtonsPreview() {
    val contact =
        Contact(
            id = 1,
            name = "John Doe",
            phoneNumber = "1234567890",
            email = "john.doe@email.com",
        )
    ContactReachOutButtons(
        contact = contact,
        onShowConfirmation = { _, _, _ ->
        },
    )
}

@Composable
fun ContactReachOutButton(
    text: String,
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .clickable {
                    onClick()
                }
                .clearAndSetSemantics {
                    contentDescription = text
                    role = Role.Button
                }
                .wrapContentSize()
                .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier =
                Modifier
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = CircleShape,
                    ).padding(10.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(text = text, style = MaterialTheme.typography.labelSmall)
    }
}

@Preview(showBackground = true)
@Composable
private fun ContactReachOutButtonPreview() {
    ContactReachOutButton(
        text = "Call",
        icon = R.drawable.call_24px,
        onClick = {},
    )
}
