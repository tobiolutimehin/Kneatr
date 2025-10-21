package com.hollowvyn.kneatr.ui.contact.detail

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hollowvyn.kneatr.R
import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import com.hollowvyn.kneatr.domain.model.CommunicationLog
import com.hollowvyn.kneatr.domain.util.DateTimeUtils
import kotlinx.datetime.LocalDate

@Composable
fun CommunicationLogItem(
    log: CommunicationLog,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isExpandable by remember { mutableStateOf(false) }

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(enabled = isExpandable) { isExpanded = !isExpanded }
                .padding(vertical = 8.dp)
                .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = log.type.icon),
            contentDescription = stringResource(id = log.type.label),
            tint = MaterialTheme.colorScheme.primary,
        )
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 4.dp),
        ) {
            Text(
                text = stringResource(id = log.type.label),
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                text = DateTimeUtils.formatDate(log.date),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (!log.notes.isNullOrBlank()) {
                Text(
                    text = log.notes,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { textLayoutResult ->
                        isExpandable = textLayoutResult.hasVisualOverflow || isExpanded
                    },
                )
            }
        }
        CommunicationLogItemDropdownMenu(onEdit = onEdit, onDelete = onDelete)
    }
}

@Composable
private fun CommunicationLogItemDropdownMenu(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showMenu by remember { mutableStateOf(false) }

    Box(modifier) {
        IconButton(onClick = { showMenu = true }) {
            Icon(
                painter = painterResource(R.drawable.more_horiz_24px),
                contentDescription = stringResource(R.string.more_options),
            )
        }
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.edit)) },
                onClick = {
                    onEdit()
                    showMenu = false
                },
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.delete)) },
                onClick = {
                    onDelete()
                    showMenu = false
                },
            )
        }
    }
}

internal fun LazyListScope.communicationLogItems(
    communicationLog: List<CommunicationLog>,
    onEditLog: (CommunicationLog) -> Unit,
    onDeleteLog: (CommunicationLog) -> Unit,
) {
    item {
        Text(
            stringResource(R.string.communication_log),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
        )
    }

    items(communicationLog.take(10)) {
        CommunicationLogItem(
            log = it,
            onEdit = { onEditLog(it) },
            onDelete = { onDeleteLog(it) },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CommunicationLogItemPreview() {
    val log =
        CommunicationLog(
            type = CommunicationType.PHONE_CALL,
            date = LocalDate(2023, 1, 15),
            contactId = 1L,
            id = 1L,
            notes = "This is a note for the communication log. It can be a bit long to see how it overflows. Clicking it will expand it.",
        )
    CommunicationLogItem(log = log, onEdit = {}, onDelete = {})
}
