package com.hollowvyn.kneatr.ui.contact.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import com.hollowvyn.kneatr.data.util.DateTimeUtils
import com.hollowvyn.kneatr.domain.model.CommunicationLog
import kotlinx.datetime.LocalDate
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun CommunicationLogItem(
    log: CommunicationLog,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .combinedClickable(
                    onLongClick = onClick,
                    onClick = {},
                ).padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = log.type.icon),
            contentDescription = stringResource(id = log.type.label),
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(id = log.type.label),
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                text = DateTimeUtils.formatDate(log.date),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (log.notes?.isNotBlank() == true) {
                Text(
                    text = log.notes,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CommunicationLogItemPreview() {
    val log =
        CommunicationLog(
            type = CommunicationType.PHONE_CALL,
            date = LocalDate(2023, 1, 15),
            contactId = 1L,
            id = 1L,
            notes = "This is a note for the communication log. It can be a bit long to see how it overflows.",
        )
    CommunicationLogItem(log = log, onClick = {})
}
