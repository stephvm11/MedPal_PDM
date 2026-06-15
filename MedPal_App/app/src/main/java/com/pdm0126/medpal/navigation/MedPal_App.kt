package com.pdm0126.medpal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.pdm0126.medpal.screens.Appoinments.AppointmentsHomeScreen
import com.pdm0126.medpal.screens.Start.StartScreen

@Composable
fun MedPal_App(modifier: Modifier){
    val backStack = rememberNavBackStack(Routes.Start)

    NavDisplay(
        backStack = backStack,
        onBack = {backStack.removeLastOrNull()},
        entryProvider = entryProvider {
            entry<Routes.Start>{
                StartScreen(onClick = {backStack.add(Routes.Appoinments)})
            }
            entry<Routes.Appoinments>{
                AppointmentsHomeScreen()
            }
        }
    )
}