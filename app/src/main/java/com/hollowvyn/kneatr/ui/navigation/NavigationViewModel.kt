package com.hollowvyn.kneatr.ui.navigation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.NavKey
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel
    @Inject
    constructor() : ViewModel() {
        val topLevelBackStack = mutableStateOf(TopLevelBackStack<NavKey>(Home))
    }
