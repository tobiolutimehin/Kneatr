package com.hollowvyn.kneatr.ui.contact.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hollowvyn.kneatr.R
import com.hollowvyn.kneatr.domain.fakes.ContactFakes
import com.hollowvyn.kneatr.domain.model.ContactTier
import com.hollowvyn.kneatr.ui.components.KneatrModalBottomSheet
import com.hollowvyn.kneatr.ui.components.KneatrSheetContent

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TierSelectorContent(
    currentTier: ContactTier?,
    allTiers: List<ContactTier>,
    onTierSelect: (ContactTier?) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    KneatrSheetContent(
        title = stringResource(R.string.select_tier),
        onCancel = onCancel,
        modifier = modifier,
    ) {
        FlowRow(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            allTiers.forEach { tier ->
                Button(
                    onClick = { onTierSelect(tier) },
                    enabled = tier != currentTier,
                ) {
                    Text(text = tier.name)
                }
            }
            currentTier?.let {
                Button(onClick = { onTierSelect(null) }) {
                    Text(text = stringResource(R.string.remove_tier))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TierSelectorBottomSheet(
    currentTier: ContactTier?,
    allTiers: List<ContactTier>,
    onSelectTier: (ContactTier?) -> Unit,
    dismissBottomSheet: () -> Unit,
    modifier: Modifier = Modifier,
) {
    KneatrModalBottomSheet(
        onDismiss = dismissBottomSheet,
        modifier = modifier,
    ) { hideSheet ->
        TierSelectorContent(
            currentTier = currentTier,
            allTiers = allTiers,
            onTierSelect = {
                onSelectTier(it)
                hideSheet()
            },
            onCancel = hideSheet,
        )
    }
}

@Preview
@Composable
private fun TierSelectorContentPreview() {
    TierSelectorContent(
        currentTier = ContactFakes.tier1,
        allTiers = listOf(ContactFakes.tier1, ContactFakes.tier2, ContactFakes.tier3),
        onTierSelect = {},
        onCancel = {},
    )
}
