package com.hollowvyn.kneatr.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.hollowvyn.kneatr.ui.contact.detail.ContactDetailScreen
import com.hollowvyn.kneatr.ui.contact.list.ContactsListScreen
import com.hollowvyn.kneatr.ui.home.HomeScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun KneatrApp(
    modifier: Modifier = Modifier,
    navViewModel: NavigationViewModel = hiltViewModel(),
) {
    val listDetailStrategy: SceneStrategy<NavKey> = rememberListDetailSceneStrategy()
    val topLevelBackStack = navViewModel.topLevelBackStack.value

    NavigationSuiteScaffold(
        navigationSuiteItems = { kneatrNavigationItems(topLevelBackStack) },
        modifier = modifier,
    ) {
        NavDisplay(
            backStack = topLevelBackStack.backStack,
            onBack = { topLevelBackStack.removeLast() },
            sceneStrategy = listDetailStrategy,
            entryProvider =
                entryProvider {
                    entry<ContactsList>(
                        metadata =
                            ListDetailSceneStrategy.listPane(
                                detailPlaceholder = { DetailPlaceholder() },
                            ),
                    ) {
                        ContactsListScreen(
                            onContactClick = { contact ->
                                topLevelBackStack.add(ContactDetail(id = contact.id))
                            },
                        )
                    }

                    entry<ContactDetail>(metadata = ListDetailSceneStrategy.detailPane()) { contactDetail ->
                        ContactDetailScreen(
                            contactId = contactDetail.id,
                            onNavigateBack = {
                                topLevelBackStack.removeLast()
                            },
                        )
                    }

                    entry<Home> {
                        HomeScreen(
                            openContact = {
                                topLevelBackStack.add(ContactDetail(id = it.id))
                            },
                        )
                    }
                },
        )
    }
}

// @Composable
// fun KneatrNavigationBar(
//    topLevelBackStack: TopLevelBackStack<NavKey>,
//    modifier: Modifier = Modifier,
// ) {
//    NavigationBar(modifier = modifier) {
//        TopLevelRoute.entries.forEach { topLevelRoute ->
//            val isSelected = topLevelRoute == topLevelBackStack.topLevelKey
//
//            NavigationBarItem(
//                selected = isSelected,
//                onClick = {
//                    topLevelBackStack.addTopLevel(topLevelRoute)
//                },
//                icon = {
//                    Icon(
//                        painter = painterResource(topLevelRoute.icon),
//                        contentDescription = stringResource(topLevelRoute.contentDescription),
//                    )
//                },
//                label = {
//                    Text(stringResource(topLevelRoute.label))
//                },
//            )
//        }
//    }
// }

private fun NavigationSuiteScope.kneatrNavigationItems(topLevelBackStack: TopLevelBackStack<NavKey>) {
    TopLevelRoute.entries.forEach { topLevelRoute ->
        val selected = topLevelRoute == topLevelBackStack.topLevelKey

        item(
            selected = selected,
            onClick = { topLevelBackStack.addTopLevel(topLevelRoute) },
            icon = {
                Icon(
                    painter = painterResource(topLevelRoute.icon),
                    contentDescription = stringResource(topLevelRoute.contentDescription),
                )
            },
            label = { Text(stringResource(topLevelRoute.label)) },
            alwaysShowLabel = false,
        )
    }
}

@Composable
fun DetailPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text("Placeholder")
    }
}
