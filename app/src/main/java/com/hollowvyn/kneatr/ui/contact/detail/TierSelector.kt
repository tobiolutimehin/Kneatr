package com.hollowvyn.kneatr.ui.contact.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hollowvyn.kneatr.domain.fakes.ContactFakes
import com.hollowvyn.kneatr.domain.model.ContactTier
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TierSelectorContent(
    currentTier: ContactTier?,
    allTiers: List<ContactTier>,
    onTierSelect: (ContactTier?) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp),
    ) {
        SheetHeader(title = "Select Tier", onCancel = onCancel)
        FlowRow(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 16.dp),
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
                    Text(text = "Remove Tier")
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
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val onCancel = {
        scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
            if (!bottomSheetState.isVisible) {
                dismissBottomSheet()
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = dismissBottomSheet,
        sheetState = bottomSheetState,
        contentWindowInsets = { BottomSheetDefaults.windowInsets },
        modifier = modifier,
    ) {
        TierSelectorContent(
            currentTier = currentTier,
            allTiers = allTiers,
            onTierSelect = { tier ->
                onSelectTier(tier)
                scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                    if (!bottomSheetState.isVisible) {
                        dismissBottomSheet()
                    }
                }
            },
            onCancel = { onCancel() },
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
