package com.hollowvyn.kneatr.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.hollowvyn.kneatr.ui.contact.ContactDetailScreen
import com.hollowvyn.kneatr.ui.contact.ContactsListScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun KneatrApp(modifier: Modifier = Modifier) {
    val listDetailStrategy: SceneStrategy<Any> = rememberListDetailSceneStrategy()
    val topLevelBackStack = remember { TopLevelBackStack<Any>(ContactsList) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                TOP_LEVEL_ROUTES.forEach { topLevelRoute ->

                    val isSelected = topLevelRoute == topLevelBackStack.topLevelKey
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            topLevelBackStack.addTopLevel(topLevelRoute)
                        },
                        icon = {
                            Icon(
                                imageVector = topLevelRoute.icon,
                                contentDescription = null,
                            )
                        },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavDisplay(
            modifier =
                Modifier
                    .padding(paddingValues = innerPadding)
                    .consumeWindowInsets(
                        WindowInsets.statusBars,
                    ),
            backStack = topLevelBackStack.backStack,
            onBack = { topLevelBackStack.removeLast() },
            sceneStrategy = listDetailStrategy,
            entryProvider =
                entryProvider {
                    entry<ContactsList>(
                        metadata =
                            ListDetailSceneStrategy.listPane(
                                detailPlaceholder = {
                                    Column(
                                        modifier =
                                            Modifier
                                                .fillMaxSize()
                                                .background(Color.Yellow.copy(alpha = 0.4f)),
                                        verticalArrangement = Arrangement.Center,
                                    ) {
                                        Text("Choose an Item from the List")
                                    }
                                },
                            ),
                    ) {
                        ContactsListScreen(
                            onContactClick = { contact ->
                                topLevelBackStack.add(ContactDetail(id = contact.id))
                            },
                        )
                    }

                    entry<ContactDetail>(
                        metadata = ListDetailSceneStrategy.detailPane(),
                    ) { contactDetail ->
                        ContactDetailScreen(
                            contactId = contactDetail.id,
                        )
                    }
                },
        )
    }
}
