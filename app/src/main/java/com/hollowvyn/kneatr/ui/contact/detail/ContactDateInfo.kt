package com.hollowvyn.kneatr.ui.contact.detail

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.hollowvyn.kneatr.R
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.domain.model.RelativeDate
import com.hollowvyn.kneatr.ui.helpers.getRelativeDateString

@Composable
fun ContactDateInfo(
    contact: Contact,
    modifier: Modifier = Modifier,
) {
    val lastDateString = contact.lastCommunicationDateRelative?.getRelativeDateString()
    val nextDateString = contact.nextCommunicationDateRelative?.getRelativeDateString()

    if (lastDateString == null && nextDateString == null) return

    val annotatedString =
        buildAnnotatedString {
            val baseStyle =
                MaterialTheme.typography.bodyMedium
                    .copy(fontWeight = FontWeight.Bold)
                    .toSpanStyle()

            if (lastDateString != null) {
                withStyle(baseStyle) {
                    append(stringResource(R.string.last_time_contacted, lastDateString))
                }
            }

            if (lastDateString != null && nextDateString != null) {
                withStyle(baseStyle) {
                    append(" • ")
                }
            }

            if (nextDateString != null) {
                val nextDateStyle =
                    if (contact.nextCommunicationDateRelative is RelativeDate.Overdue) {
                        baseStyle.copy(color = MaterialTheme.colorScheme.error)
                    } else {
                        baseStyle
                    }
                withStyle(nextDateStyle) {
                    append(stringResource(R.string.next_contact_in, nextDateString))
                }
            }
        }

    Text(
        text = annotatedString,
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun ContactDateInfoPreview() {
    val contact =
        Contact(
            id = 1,
            name = "John Doe",
            phoneNumber = "1234567890",
            email = "john.doe@example.com",
        )
    MaterialTheme {
        ContactDateInfo(
            contact = contact,
        )
    }
}
