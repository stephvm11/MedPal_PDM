package com.pdm0126.medpal.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.pdm0126.medpal.R

@Composable
fun AppScaffold(
    title: String,
    topBarScreenCase: TopBarCases = TopBarCases.DEFAULT,
    showBottomBar: Boolean = true,
    currentRoute: String = "",
    onNavigationItemClick: (String) -> Unit = {},
    onCalendarClick: () -> Unit = {},
    onUserClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onCloseClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    isSaveEnabled: Boolean = true,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {

    Scaffold(
        topBar = {
            CustomTopBar(
                title = title,
                case = topBarScreenCase,
                onUserClick = onUserClick,
                onCalendarClick = onCalendarClick,
                onSaveClick = onSaveClick,
                onCloseClick = onCloseClick,
                onBackClick = onBackClick,
                isSaveEnabled = isSaveEnabled
            )
        },
        floatingActionButton = floatingActionButton,

        bottomBar = {
            if (showBottomBar) {
                CustomNavigationBar(
                    currentRoute = currentRoute, onItemClick = onNavigationItemClick
                )
            }
        },
    ) { paddingValues ->
        content(paddingValues)
    }
}