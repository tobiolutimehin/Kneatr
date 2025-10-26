package com.hollowvyn.kneatr.ui.contact.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.hollowvyn.kneatr.R
import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import com.hollowvyn.kneatr.domain.model.CommunicationLog
import com.hollowvyn.kneatr.domain.util.DateTimeUtils
import com.hollowvyn.kneatr.ui.components.dialog.KneatrModalBottomSheet
import com.hollowvyn.kneatr.ui.components.dialog.KneatrSheetContent
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunicationLogBottomSheet(
    onSave: (id: Long?, date: LocalDate, type: CommunicationType, notes: String) -> Unit,
    dismissBottomSheet: () -> Unit,
    modifier: Modifier = Modifier,
    logToEdit: CommunicationLog? = null,
) {
    var notes by remember(logToEdit) { mutableStateOf(logToEdit?.notes ?: "") }
    var selectedType by remember(logToEdit) { mutableStateOf(logToEdit?.type) }
    val initialDate =
        logToEdit?.let { DateTimeUtils.toEpochMillis(it.date) } ?: System.currentTimeMillis()
    var selectedDateMillis by remember(logToEdit) { mutableLongStateOf(initialDate) }

    val title =
        if (logToEdit == null) stringResource(R.string.add_communication_log) else stringResource(R.string.edit_communication_log)

    KneatrModalBottomSheet(
        onDismiss = dismissBottomSheet,
        modifier = modifier,
    ) { hideSheet ->
        KneatrSheetContent(
            title = title,
            onCancel = hideSheet,
        ) {
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
                            logToEdit?.id,
                            DateTimeUtils.toLocalDate(selectedDateMillis),
                            type,
                            notes,
                        )
                    }
                    hideSheet()
                },
                enabled = selectedType != null,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
            text = stringResource(R.string.notes_optional),
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
