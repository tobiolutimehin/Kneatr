package com.hollowvyn.kneatr.ui.contact.detail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.hollowvyn.kneatr.R
import com.hollowvyn.kneatr.data.local.entity.CommunicationType
import com.hollowvyn.kneatr.domain.model.Contact
import com.hollowvyn.kneatr.ui.theme.KneatrTheme
import com.hollowvyn.kneatr.ui.util.startEmail
import com.hollowvyn.kneatr.ui.util.startPhoneCall
import com.hollowvyn.kneatr.ui.util.startTextMessage

@Composable
internal fun ContactReachOutButtons(
    contact: Contact,
    onShowConfirmation: (String, CommunicationType, () -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Row(
        modifier = modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
    ) {
        if (contact.phoneNumber.isNotBlank()) {
            ContactReachOutButton(
                text = context.getString(R.string.call),
                icon = R.drawable.call_24px,
                onClick = {
                    onShowConfirmation(
                        context.getString(R.string.call_x_contact, contact.name),
                        CommunicationType.PHONE_CALL,
                    ) {
                        context.startPhoneCall(contact.phoneNumber)
                    }
                },
            )

            ContactReachOutButton(
                text = context.getString(R.string.message),
                icon = R.drawable.chat_bubble_24px,
                onClick = {
                    onShowConfirmation(
                        context.getString(R.string.message_x_contact, contact.name),
                        CommunicationType.MESSAGE,
                    ) {
                        context.startTextMessage(contact.phoneNumber)
                    }
                },
            )
        }

        contact.email?.let { email ->
            ContactReachOutButton(
                text = context.getString(R.string.email),
                icon = R.drawable.mail_24px,
                onClick = {
                    onShowConfirmation(
                        context.getString(R.string.email_x_contact, contact.name),
                        CommunicationType.EMAIL,
                    ) {
                        context.startEmail(email)
                    }
                },
            )
        }
    }
}

@Composable
private fun ContactReachOutButton(
    text: String,
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .clickable {
                    onClick()
                }.clearAndSetSemantics {
                    contentDescription = text
                    role = Role.Button
                }.wrapContentSize()
                .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier =
                Modifier
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = CircleShape,
                    )
                    .padding(10.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(text = text, style = MaterialTheme.typography.labelSmall)
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun ContactReachOutButtonsPreview() {
    val contact =
        Contact(
            id = 1,
            name = "John Doe",
            phoneNumber = "1234567890",
            email = "john.doe@email.com",
        )
    KneatrTheme {
        ContactReachOutButtons(
            contact = contact,
            onShowConfirmation = { _, _, _ ->
            },
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
        )
    }
}

@PreviewLightDark
@Composable
private fun ContactReachOutButtonPreview() {
    KneatrTheme {
        ContactReachOutButton(
            text = "Call",
            icon = R.drawable.call_24px,
            onClick = {},
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
        )
    }
}
