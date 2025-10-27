package com.hollowvyn.kneatr.ui.contact.detail

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hollowvyn.kneatr.domain.fakes.ContactFakes

@Composable
fun ContactDetailNameTitle(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = name,
        style = MaterialTheme.typography.headlineLarge,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun ContactDetailNameTitlePreview() {
    ContactDetailNameTitle(name = ContactFakes.basicContact.name)
}
