package com.hollowvyn.kneatr.ui.contact.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CharacterHeader(
    letter: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Text(
        text = letter,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                ).clickable { onClick() }
                .padding(8.dp),
    )
}

@Preview
@Composable
private fun CharacterHeaderPreview() {
    CharacterHeader(letter = "A")
}
