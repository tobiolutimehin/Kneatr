package com.hollowvyn.kneatr.ui.contact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hollowvyn.kneatr.domain.model.ContactTier

@Composable
fun ContactTierPill(
    tier: ContactTier?,
    modifier: Modifier = Modifier,
) {
    tier?.let {
        Text(
            text = it.name,
            modifier =
                modifier
                    .background(Color.Blue, RoundedCornerShape(50))
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Preview
@Composable
private fun ContactTierPillPreview() {
    val tier = ContactTier(id = 1, name = "Tier 1", daysBetweenContact = 7)
    ContactTierPill(tier)
}
