package com.hollowvyn.kneatr.ui.contact.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hollowvyn.kneatr.R
import com.hollowvyn.kneatr.domain.model.ContactTier

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactTierPill(
    tier: ContactTier?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    if (enabled || tier != null) {
        Text(
            text = tier?.name ?: stringResource(R.string.add_tier),
            modifier =
                modifier
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .then(
                        if (enabled) {
                            Modifier
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                .combinedClickable(onClick = onClick, onLongClick = onLongClick)
                        } else {
                            Modifier
                        },
                    ).padding(horizontal = 6.dp, vertical = 2.dp),
            style = if (enabled) MaterialTheme.typography.titleMedium else MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }
}

@Preview
@Composable
private fun ContactTierPillPreview() {
    val tier = ContactTier(id = 1, name = "Tier 1", daysBetweenContact = 7)
    ContactTierPill(tier)
}
