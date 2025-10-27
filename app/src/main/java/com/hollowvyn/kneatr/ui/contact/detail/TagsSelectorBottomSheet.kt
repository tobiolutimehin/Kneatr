package com.hollowvyn.kneatr.ui.contact.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hollowvyn.kneatr.domain.model.ContactTag
import com.hollowvyn.kneatr.ui.components.dialog.KneatrModalBottomSheet
import com.hollowvyn.kneatr.ui.components.dialog.KneatrSheetContent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TagsSelectorBottomSheet(
    currentTags: Set<ContactTag>,
    allTags: List<ContactTag>,
    onSave: (Set<ContactTag>) -> Unit,
    dismissBottomSheet: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TagSelectorViewModel = hiltViewModel(),
) {
    val selectedTags by viewModel.selectedTags.collectAsState()
    val tagInput by viewModel.tagInput.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    val hasChanges by viewModel.hasChanges.collectAsState()
    val focusManager = LocalFocusManager.current

    KneatrModalBottomSheet(onDismiss = dismissBottomSheet, modifier = modifier) {
        KneatrSheetContent(
            title = "Tag Selector",
            onCancel = dismissBottomSheet,
            onSave = {
                onSave(viewModel.getSelectedTags())
                dismissBottomSheet()
            },
            saveEnabled = hasChanges,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = tagInput,
                        onValueChange = { viewModel.onTagInputChanged(it) },
                        label = { Text("Add or find a tag") },
                        placeholder = { Text("e.g., family, work_colleague") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(
                                onClick = { viewModel.addTag(tagInput) },
                                enabled = tagInput.isNotBlank(),
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add Tag")
                            }
                        },
                        keyboardOptions =
                            KeyboardOptions(
                                capitalization = KeyboardCapitalization.None,
                                imeAction = ImeAction.Done,
                                autoCorrectEnabled = false,
                            ),
                        keyboardActions =
                            KeyboardActions(
                                onDone = {
                                    viewModel.addTag(tagInput)
                                    focusManager.clearFocus()
                                },
                            ),
                        singleLine = true,
                    )

                    if (suggestions.isNotEmpty()) {
                        LazyRow(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(suggestions) { tag ->
                                SuggestionChip(
                                    onClick = { viewModel.addTag(tag.name) },
                                    label = { Text(tag.name) },
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))

                Text("Selected Tags", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    if (selectedTags.isEmpty()) {
                        Text(
                            "No tags selected.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    } else {
                        selectedTags.forEach { tag ->
                            InputChip(
                                selected = true,
                                onClick = { /* Could be used for editing a tag later */ },
                                label = { Text(tag.name) },
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Remove Tag",
                                        modifier =
                                            Modifier
                                                .size(InputChipDefaults.IconSize)
                                                .clickable {
                                                    viewModel.removeTag(tag)
                                                },
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(currentTags, allTags) {
        viewModel.initialize(currentTags, allTags)
    }
}
