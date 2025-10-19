package com.hollowvyn.kneatr.ui.contact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hollowvyn.kneatr.R
import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import com.hollowvyn.kneatr.data.util.DateTimeUtils
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.time.ExperimentalTime

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

@OptIn(ExperimentalTime::class)
@Composable
fun CommunicationLogSheetContent(
    onSave: (LocalDate, CommunicationType, String) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var notes by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<CommunicationType?>(null) }
    var selectedDateMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SheetHeader(onCancel = onCancel)

        DateSelector(
            selectedDateMillis = selectedDateMillis,
            onDateChange = { selectedDateMillis = it },
        )

        CommunicationTypeSelector(
            selectOption = { selectedType = it },
            selectedOption = selectedType,
        )

        NotesInput(
            notes = notes,
            onNotesChange = { notes = it },
        )

        Button(
            onClick = {
                selectedType?.let { type ->
                    onSave(
                        DateTimeUtils.toLocalDate(selectedDateMillis),
                        type,
                        notes,
                    )
                }
            },
            enabled = selectedType != null,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.save))
        }
    }
}

@Composable
private fun SheetHeader(
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.add_communication_log),
            style = MaterialTheme.typography.titleLarge,
        )
        IconButton(
            onClick = onCancel,
        ) {
            Icon(Icons.Default.Close, contentDescription = stringResource(R.string.close))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
private fun DateSelector(
    selectedDateMillis: Long,
    onDateChange: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val datePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis = selectedDateMillis,
            selectableDates = DateTimeUtils.getSelectablePastAndPresentDates(),
        )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(stringResource(R.string.date), style = MaterialTheme.typography.titleMedium)
        InputChip(
            selected = false,
            onClick = { showDatePickerDialog = true },
            label = {
                Text(
                    text = DateTimeUtils.formatDate(selectedDateMillis),
                )
            },
        )
    }

    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePickerDialog = false
                        datePickerState.selectedDateMillis?.let(onDateChange)
                    },
                ) { Text(stringResource(R.string.ok)) }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePickerDialog = false },
                ) { Text(stringResource(R.string.cancel)) }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun CommunicationTypeSelector(
    modifier: Modifier = Modifier,
    selectOption: (CommunicationType) -> Unit = { },
    selectedOption: CommunicationType? = null,
) {
    val options = CommunicationType.entries
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.type_of_communication),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(),
        )
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    shape =
                        SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size,
                        ),
                    onClick = { selectOption(option) },
                    selected = option == selectedOption,
                    label = {
                        Icon(
                            painter = painterResource(option.icon),
                            contentDescription = stringResource(option.label),
                        )
                    },
                )
            }
        }
    }
}

@Composable
fun NotesInput(
    notes: String,
    onNotesChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.notes),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = notes,
            onValueChange = onNotesChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            minLines = 2,
            maxLines = 2,
            placeholder = {
                Text(
                    stringResource(R.string.communication_log_sheet_notes_placeholder),
                    style = MaterialTheme.typography.bodySmall,
                )
            },
        )
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
