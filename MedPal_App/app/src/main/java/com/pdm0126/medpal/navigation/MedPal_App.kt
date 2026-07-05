package com.pdm0126.medpal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.pdm0126.medpal.ui.screens.Appoinments.AppointmentsHomeScreen
import com.pdm0126.medpal.ui.screens.Start.StartScreen

@Composable
fun MedPal_App(
    userName: String?,
    onLogout: () -> Unit
){
    val backStack = rememberNavBackStack(Routes.Appoinments)

    NavDisplay(
        backStack = backStack,
        onBack = {backStack.removeLastOrNull()},
        entryProvider = entryProvider {
            entry<Routes.Appoinments>{
                AppointmentsHomeScreen(
                    onLogout = onLogout
                )
            }
        }
    )
}