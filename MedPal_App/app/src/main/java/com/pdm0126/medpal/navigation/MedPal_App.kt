package com.pdm0126.medpal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.pdm0126.medpal.ui.screens.AddAppointment.AddAppointmentScreen
import com.pdm0126.medpal.ui.screens.Appoinments.AppointmentsHomeScreen
import com.pdm0126.medpal.ui.screens.AddMed.AddMedicationScreen
import com.pdm0126.medpal.ui.screens.Meds.MedsHomeScreen

@Composable
fun MedPal_App(
    onLogout: () -> Unit
) {
    val backStack = rememberNavBackStack(Routes.Appoinments)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Routes.Appoinments> {
                AppointmentsHomeScreen(
                    onLogout = onLogout,
                    currentRoute = "appointments",
                    onNavigateToItemClick = { route ->
                        if (route == "medication") {
                            backStack.add(Routes.Meds)
                        }
                    },
                    onAddAppointmentClick = {backStack.add(Routes.AddAppointmentForm)}
                )
            }
            entry<Routes.Meds> {
                MedsHomeScreen(
                    onNavigateToAddMedication = { backStack.add(Routes.MedsAddForm) },
                    onNavigateToProfile = onLogout,
                    currentRoute = "medication",
                    onNavigateToItemClick = { route ->
                        if (route == "appointments") {
                            backStack.add(Routes.Appoinments)
                        }
                    }
                )
            }
            entry<Routes.MedsAddForm> {
                AddMedicationScreen(
                    onCancel = {
                        backStack.removeLastOrNull()
                    }
                )
            }
            entry<Routes.AddAppointmentForm> {
                AddAppointmentScreen(
                    onSave = {
                        backStack.removeLastOrNull()
                    },
                    onClose = {
                        backStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}