package com.hollowvyn.kneatr.ui.contact.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hollowvyn.kneatr.R
import com.hollowvyn.kneatr.domain.fakes.ContactFakes
import com.hollowvyn.kneatr.domain.model.ContactTier
import com.hollowvyn.kneatr.ui.components.dialog.KneatrModalBottomSheet
import com.hollowvyn.kneatr.ui.components.dialog.KneatrSheetContent

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
        KneatrSheetContent(
            title = stringResource(R.string.select_tier),
            onCancel = hideSheet,
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
                    InputChip(
                        selected = tier == currentTier,
                        onClick = {
                            onSelectTier(tier)
                            hideSheet()
                        },
                        label = { Text(text = tier.name) },
                    )
                }
                currentTier?.let {
                    TextButton(
                        onClick = {
                            onSelectTier(null)
                            hideSheet()
                        },
                    ) {
                        Text(text = stringResource(R.string.remove_tier))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TierSelectorContentPreview() {
    TierSelectorBottomSheet(
        currentTier = ContactFakes.tier1,
        allTiers = listOf(ContactFakes.tier1, ContactFakes.tier2, ContactFakes.tier3),
        onSelectTier = {},
        dismissBottomSheet = {},
    )
}
