package com.hollowvyn.kneatr.ui.contact

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hollowvyn.kneatr.domain.model.ContactDto
import com.hollowvyn.kneatr.ui.contact.viewmodel.ContactDetailViewModel

sealed class ContactDetailUiState {
    data class Success(
        val contact: ContactDto?,
    ) : ContactDetailUiState()

    data object Error : ContactDetailUiState()

    data object Loading : ContactDetailUiState()
}

@Composable
fun ContactDetailScreen(
    contactId: Long,
    modifier: Modifier = Modifier,
    viewModel: ContactDetailViewModel = hiltViewModel<ContactDetailViewModel>(),
) {
    LaunchedEffect(key1 = contactId) {
        viewModel.loadContactId(contactId)
    }

    val uiState by viewModel.uiState.collectAsState()

    (uiState as? ContactDetailUiState.Success)?.contact?.let {
        Column(modifier = modifier.padding(16.dp)) {
            Text(text = "Name: ${it.name}")
            Text(text = "Phone: ${it.phoneNumber}")
            it.email?.let { email ->
                Text(text = "Email: $email")
            }
        }
    }
}
