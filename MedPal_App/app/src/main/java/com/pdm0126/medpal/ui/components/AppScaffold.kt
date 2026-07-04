package com.pdm0126.medpal.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun AppScaffold(
    title: String,
    topBarScreenCase: TopBarCases = TopBarCases.DEFAULT,
    onCalendarClick: () -> Unit = {},
    onUserClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onCloseClick: () -> Unit = {},
    onBackClick: () -> Unit = {},

    floatingActionButton: @Composable () -> Unit = {},

    content: @Composable (PaddingValues) -> Unit
) {
    var currentRoute by rememberSaveable { mutableStateOf("Citas y Examenes") }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = title,
                case = topBarScreenCase,
                onUserClick = onUserClick,
                onCalendarClick = onCalendarClick,
                onSaveClick = onSaveClick,
                onCloseClick = onCloseClick,
                onBackClick = onBackClick
            )
        },
        floatingActionButton = floatingActionButton,

        bottomBar = {
            CustomNavigationBar(currentRoute = currentRoute, onItemClick = {currentRoute = it})
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}