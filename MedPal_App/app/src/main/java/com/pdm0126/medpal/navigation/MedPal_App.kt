package com.pdm0126.medpal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.pdm0126.medpal.ui.screens.Appoinments.AppointmentsHomeScreen
import com.pdm0126.medpal.ui.screens.Meds.MedsHomeScreen
import com.pdm0126.medpal.ui.screens.Start.StartScreen
import okhttp3.Route

@Composable
fun MedPal_App(
    userName: String?,
    onLogout: () -> Unit
){
    val backStack = rememberNavBackStack(Routes.Meds)

    NavDisplay(
        backStack = backStack,
        onBack = {backStack.removeLastOrNull()},
        entryProvider = entryProvider {
            entry<Routes.Appoinments>{
                AppointmentsHomeScreen(
                    onLogout = onLogout,
                    userName = userName,
                    currentRoute = "appointments",
                    onNavigateToItemClick = {route ->
                        if (route == "medication"){
                            backStack.add(Routes.Meds)
                        }
                    }
                )
            }
            entry<Routes.Meds>{
                MedsHomeScreen(
                    onNavigateToAddMedication = {},
                    onNavigateToProfile = onLogout,
                    currentRoute = "medication",
                    onNavigateToItemClick = {route ->
                        if (route == "appointments") {
                            backStack.add(Routes.Appoinments)
                        }
                    }
                )
            }
        }

    )
}