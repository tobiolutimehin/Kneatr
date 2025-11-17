package com.hollowvyn.kneatr.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hollowvyn.kneatr.R
import com.hollowvyn.kneatr.domain.fakes.ContactFakes
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.ui.helpers.getRelativeDateString

@Composable
internal fun HomeContactCard(
    contact: Contact,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onMarkAsComplete: () -> Unit = {},
) {
    ListItem(
        modifier = modifier.clickable { onClick() },
        headlineContent = { Text(text = contact.name) },
        supportingContent = {
            contact.lastCommunicationDateRelative?.getRelativeDateString()?.let {
                val text = stringResource(R.string.last_time_contacted, it)
                Text(text = text, maxLines = 2)
            }
        },
        trailingContent = {
            if (contact.reachedOutToday) {
                Text(
                    text = "Reached out today!",
                    style = MaterialTheme.typography.labelSmall,
                )
            } else {
                TextButton(
                    onClick = onMarkAsComplete,
                    content = {
                        Text(
                            text = "Mark as Complete",
                            style = MaterialTheme.typography.labelSmall,
                        )
                    },
                )
            }
        },
    )
}

@Preview
@Composable
private fun HomeContactCardPreview() {
    HomeContactCard(
        contact = ContactFakes.contactWithLogs,
    )
}
