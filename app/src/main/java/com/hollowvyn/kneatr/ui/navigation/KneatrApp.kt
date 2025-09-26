package com.hollowvyn.kneatr.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.hollowvyn.kneatr.ui.contact.ContactsListScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun KneatrApp(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(ContactsList)
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()

    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier.padding(paddingValues = innerPadding),
            backStack = backStack,
            onBack = { keysToRemove -> repeat(keysToRemove) { backStack.removeLastOrNull() } },
            sceneStrategy = listDetailStrategy,
            entryProvider =
                entryProvider {
                    entry<ContactsList>(
                        metadata =
                            ListDetailSceneStrategy.listPane(
                                detailPlaceholder = {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                    )
                                },
                            ),
                    ) {
                        ContactsListScreen()
                    }

                    entry<ContactDetail>(
                        metadata = ListDetailSceneStrategy.detailPane(),
                    ) {
                    }
                },
        )
    }
}
