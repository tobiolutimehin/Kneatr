package com.hollowvyn.kneatr.ui.contact

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import com.hollowvyn.kneatr.data.local.typeconverter.LocalDateConverters
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunicationLogBottomSheet(
    addCommunicationLog: (LocalDate, CommunicationType, String) -> Unit,
    dismissBottomSheet: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = dismissBottomSheet,
        sheetState = bottomSheetState,
        contentWindowInsets = { BottomSheetDefaults.windowInsets },
        modifier = modifier,
    ) {
        CommunicationLogSheetContent(
            onSave = { date, type, notes ->
                addCommunicationLog(date, type, notes)
                scope
                    .launch {
                        bottomSheetState.hide()
                    }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            dismissBottomSheet()
                        }
                    }
            },
            onCancel = {
                scope
                    .launch {
                        bottomSheetState.hide()
                    }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            dismissBottomSheet()
                        }
                    }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunicationLogSheetContent(
    onSave: (LocalDate, CommunicationType, String) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var notes by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<CommunicationType?>(null) }

    val initialDateMillis = System.currentTimeMillis()
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var selectedDateMillis by remember { mutableLongStateOf(initialDateMillis) }

    val formatter = remember { DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM) }

    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Add Communication Log",
                style = MaterialTheme.typography.titleLarge,
            )
            IconButton(
                onClick = onCancel,
                modifier = Modifier,
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Date", style = MaterialTheme.typography.titleMedium)
            InputChip(
                selected = false,
                onClick = { showDatePickerDialog = true },
                label = {
                    Text(
                        text =
                            Instant
                                .ofEpochMilli(selectedDateMillis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                                .format(formatter),
                    )
                },
            )
        }

        Text(
            text = "Type of Communication",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(),
        )
        CommunicationTypeSelector(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
            selectOption = { selectedType = it },
            selectedOption = selectedType,
        )
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            minLines = 2,
            maxLines = 2,
        )

        Button(
            onClick = {
                selectedType?.let { type ->
                    onSave(
                        LocalDateConverters.fromEpochDays(selectedDateMillis.toInt())!!,
                        type,
                        notes,
                    )
                }
            },
            enabled = selectedType != null,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Save")
        }
    }

    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePickerDialog = false
                        selectedDateMillis = datePickerState.selectedDateMillis ?: initialDateMillis
                    },
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePickerDialog = false },
                ) { Text("Cancel") }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CommunicationLogSheetContentPreview() {
    CommunicationLogSheetContent(
        onSave = { _, _, _ -> },
        onCancel = { },
    )
}

@Composable
fun CommunicationTypeSelector(
    modifier: Modifier = Modifier,
    selectOption: (CommunicationType) -> Unit = { },
    selectedOption: CommunicationType? = null,
) {
    val options = CommunicationType.entries
    SingleChoiceSegmentedButtonRow(
        modifier = modifier,
    ) {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                shape =
                    SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size,
                    ),
                onClick = { selectOption(option) },
                selected = option == selectedOption,
                label = { Text(text = option.name, style = MaterialTheme.typography.labelSmall) },
            )
        }
    }
}
