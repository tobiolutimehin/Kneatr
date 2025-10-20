package com.hollowvyn.kneatr.ui.contact.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.hollowvyn.kneatr.R
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.domain.model.RelativeDate
import com.hollowvyn.kneatr.ui.helpers.getRelativeDateString
import kotlinx.datetime.LocalDate

@Composable
fun ContactDateInfo(
    contact: Contact,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        with(contact) {
            lastContactedRelatedDate?.let {
                val relativeLastDate = it.getRelativeDateString()
                Text(
                    text = stringResource(R.string.last_time_contacted, relativeLastDate),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

            nextContactDateRelated?.let {
                val relativeNextDateString = it.getRelativeDateString()

                val textColor =
                    if (it is RelativeDate.Overdue) {
                        MaterialTheme.colorScheme.error
                    } else {
                        Color.Unspecified
                    }

                Text(
                    text = stringResource(R.string.next_contact_in, relativeNextDateString),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                )
            }
        }
    }
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
            lastDate = LocalDate.parse("2023-10-26"),
            nextContactDate = LocalDate.parse("2023-11-10"),
        )
    MaterialTheme {
        ContactDateInfo(
            contact = contact,
        )
    }
}
