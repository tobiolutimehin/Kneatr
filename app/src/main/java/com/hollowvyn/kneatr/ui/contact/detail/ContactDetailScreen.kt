package com.hollowvyn.kneatr.ui.contact.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedSuggestionChip
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import com.hollowvyn.kneatr.domain.fakes.ContactFakes
import com.hollowvyn.kneatr.domain.model.CommunicationLog
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.domain.model.ContactTag
import com.hollowvyn.kneatr.domain.util.DateTimeUtils
import com.hollowvyn.kneatr.domain.util.Logger
import com.hollowvyn.kneatr.ui.components.ContactTierPill
import com.hollowvyn.kneatr.ui.components.dialog.DeepInteractionConfirmationDialog
import com.hollowvyn.kneatr.ui.contact.detail.viewmodel.ContactDetailUiState
import com.hollowvyn.kneatr.ui.contact.detail.viewmodel.ContactDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailScreen(
    contactId: Long,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ContactDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val tiers by viewModel.tiers.collectAsState()
    val allTags by viewModel.allTags.collectAsState()

    val listState = rememberLazyListState()
    val isScrolled = remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }

    var showCommunicationLogSheet by remember { mutableStateOf(false) }
    var showTagsSheet by remember { mutableStateOf(false) }
    var showTierSheet by remember { mutableStateOf(false) }
    var selectedLog by remember { mutableStateOf<CommunicationLog?>(null) }

    var showConfirmationDialog by remember { mutableStateOf(false) }
    var confirmationAction by remember { mutableStateOf({}) }
    var confirmationText by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    val title = (uiState as? ContactDetailUiState.Success)?.contact?.name ?: ""
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
                    selectedLog = null
                    showCommunicationLogSheet = true
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
                ContactDetailContent(
                    contact = state.contact,
                    modifier = Modifier.padding(innerPadding),
                    listState = listState,
                    onEditLog = { log ->
                        selectedLog = log
                        showCommunicationLogSheet = true
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
                    onEditTier = { remove ->
                        if (remove) {
                            viewModel.updateTier(null)
                        } else {
                            showTierSheet = true
                        }
                    },
                    onEditTags = {
                        showTagsSheet = true
                    },
                )
                Logger.i(
                    tag = TAG,
                    message = "Contact loaded successfully: ${state.contact.name}",
                )
            }

            ContactDetailUiState.Error -> {
                Text(
                    "Error loading contact",
                    modifier = Modifier.padding(innerPadding),
                )
                Logger.e(
                    tag = TAG,
                    message = "Error loading contact",
                )
            }

            ContactDetailUiState.Loading ->
                Text(
                    "Loading...",
                    modifier = Modifier.padding(innerPadding),
                )
        }

        if (showCommunicationLogSheet) {
            CommunicationLogBottomSheet(
                logToEdit = selectedLog,
                onSave = { id, date, type, notes ->
                    if (id == null) {
                        viewModel.addCommunicationLog(date, type, notes)
                    } else {
                        viewModel.updateCommunicationLog(date, type, notes, id)
                    }
                },
                dismissBottomSheet = { showCommunicationLogSheet = false },
            )
        }

        if (showTierSheet) {
            (uiState as? ContactDetailUiState.Success)?.contact?.let { contact ->
                TierSelectorBottomSheet(
                    currentTier = contact.tier,
                    allTiers = tiers,
                    onSelectTier = { viewModel.updateTier(it) },
                    dismissBottomSheet = { showTierSheet = false },
                )
            }
        }

        if (showConfirmationDialog) {
            DeepInteractionConfirmationDialog(
                onConfirm = confirmationAction,
                onDismiss = { showConfirmationDialog = false },
                text = confirmationText,
            )
        }

        if (showTagsSheet) {
            (uiState as? ContactDetailUiState.Success)?.contact?.let { contact ->
                TagsSelectorBottomSheet(
                    currentTags = contact.tags,
                    allTags = allTags.toMutableList(),
                    onSave = { updatedTags ->
                        viewModel.updateTags(updatedTags)
                    },
                    dismissBottomSheet = { showTagsSheet = false },
                )
            }
        }
    }

    LaunchedEffect(key1 = contactId) {
        viewModel.loadContactId(contactId)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ContactDetailContent(
    contact: Contact,
    listState: LazyListState,
    onEditLog: (CommunicationLog) -> Unit,
    onDeleteLog: (CommunicationLog) -> Unit,
    onShowConfirmation: (String, CommunicationType, () -> Unit) -> Unit,
    onEditTier: (remove: Boolean) -> Unit,
    onEditTags: () -> Unit,
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
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Text(
                text = contact.name,
                style = MaterialTheme.typography.headlineLarge,
            )
        }
        item {
            ContactTierPill(
                tier = contact.tier,
                enabled = true,
                onClick = { onEditTier(false) },
                onLongClick = { onEditTier(true) },
            )
        }

        item {
            TagsSection(
                tags = contact.tags,
                onEditTags = onEditTags,
            )
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

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TagsSection(
    tags: Set<ContactTag>,
    onEditTags: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        stickyHeader {
            ElevatedSuggestionChip(
                onClick = onEditTags,
                label = { Text(text = "Edit Tags") },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                    )
                },
            )
        }

        items(tags.toList()) {
            ElevatedSuggestionChip(
                onClick = {},
                enabled = false,
                label = {
                    Text(text = it.name)
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ContactDetailContentPreview() {
    ContactDetailContent(
        contact = ContactFakes.fullContact,
        listState = rememberLazyListState(),
        onEditLog = {},
        onDeleteLog = {},
        onShowConfirmation = { _, _, _ -> },
        onEditTier = {},
        onEditTags = {},
        modifier = Modifier,
    )
}

private const val TAG = "ContactDetailScreen"
