package com.hollowvyn.kneatr.ui.home.viewmodel

import com.hollowvyn.kneatr.domain.model.Contact

enum class HomeScreenSection {
    Overdue(
        title = "Overdue",
        filter = { it.overdueContacts },
    ),
    Random(
        title = "Random",
        filter = { it.randomContacts },
        onRefresh = { /*TODO*/ },
    ),
    Upcoming(
        title = "Upcoming",
        filter = { it.upcomingContacts },
    ),
    DueToday(
        title = "Contacts Due Today",
        filter = { it.contactDueToday },
    ),
    ;

    val title: String
    val filter: (HomeUiState.Success) -> List<Contact>
    val onRefresh: (() -> Unit)?

    constructor(
        title: String,
        filter: (HomeUiState.Success) -> List<Contact>,
        onRefresh: (() -> Unit)? = null,
    ) {
        this.title = title
        this.filter = filter
        this.onRefresh = onRefresh
    }
}
