package com.hollowvyn.kneatr.ui.contact.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hollowvyn.kneatr.domain.model.ContactTier
import com.hollowvyn.kneatr.domain.util.formatPhoneNumber

@Composable
fun ContactListItem(
    name: String,
    phoneNumber: String,
    tier: ContactTier?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
            ) {
                Text(text = name, style = MaterialTheme.typography.headlineSmall)
                tier?.let { ContactTierPill(it) }
            }
        },
        supportingContent = {
            Text(
                text = phoneNumber.formatPhoneNumber(),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        trailingContent = {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
            )
        },
        modifier = modifier.clickable { onClick() },
    )
}

@Preview
@Composable
private fun ContactListItemPreview() {
    val name = "John Doe"
    val phoneNumber = "123-456-7890"
    val tier = ContactTier(id = 1, name = "Tier 1", daysBetweenContact = 7)
    ContactListItem(
        name = name,
        phoneNumber = phoneNumber,
        tier = tier,
    )
}
