package com.pdm0126.medpal.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.pdm0126.medpal.data.model.AlertData
import com.pdm0126.medpal.data.model.AlertType
import com.pdm0126.medpal.data.notifications.AlertGlobalEvent
import com.pdm0126.medpal.ui.screens.AddAppointment.AddAppointmentScreen
import com.pdm0126.medpal.ui.screens.Appoinments.AppointmentsHomeScreen
import com.pdm0126.medpal.ui.screens.AddMed.AddMedicationScreen
import com.pdm0126.medpal.ui.screens.Meds.MedsHomeScreen
import com.pdm0126.medpal.ui.components.MedPalAlert
import com.pdm0126.medpal.ui.screens.AddExam.AddExamScreen
import com.pdm0126.medpal.ui.screens.Profile.ProfileScreen


@Composable
fun MedPal_App(
    onLogout: () -> Unit
) {
    val backStack = rememberNavBackStack(Routes.Appoinments)
    var activeAlert by remember { mutableStateOf<AlertData?>(null) }

    LaunchedEffect(Unit) {
        AlertGlobalEvent.events.collect { data ->
            activeAlert = data
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<Routes.Appoinments> {
                    AppointmentsHomeScreen(
                        onNavigateToProfile = { backStack.add(Routes.Profile) },
                        currentRoute = "appointments",
                        onNavigateToItemClick = { route ->
                            if (route == "medication") {
                                backStack.add(Routes.Meds)
                            }
                        },
                        onAddAppointmentClick = { backStack.add(Routes.AddAppointmentForm) },
                        onAddExamClick = { backStack.add(Routes.AddExamForm) }
                    )
                }
                entry<Routes.Meds> {
                    MedsHomeScreen(
                        onNavigateToAddMedication = { backStack.add(Routes.MedsAddForm) },
                        onNavigateToProfile = { backStack.add(Routes.Profile) },
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
                entry<Routes.AddExamForm> {
                    AddExamScreen(
                        onSave = {
                            backStack.removeLastOrNull()
                        },
                        onClose = {
                            backStack.removeLastOrNull()
                        }
                    )
                }

                entry<Routes.Profile> {
                    ProfileScreen(
                        onCloseClick = { backStack.removeLastOrNull() },
                        onLogoutClick = onLogout
                    )
                }

            }
        )
        activeAlert?.let { alert ->
            MedPalAlert(
                title = alert.title,
                subtitle = alert.subtitle,
                onConfirm = {
                    when (alert.type) {
                        AlertType.MEDICAMENTO -> {
                            AlertGlobalEvent.confirmMedicationTake(alert.id)
                        }

                        AlertType.CITA -> {
                            AlertGlobalEvent.confirmAppointment(alert.id)
                        }

                        AlertType.EXAMEN -> {
                            AlertGlobalEvent.confirmExam(alert.id)
                        }
                    }
                    activeAlert = null
                },
                onOmit = {
                    activeAlert = null
                }
            )
        }
    }
}