package com.pdm0126.medpal.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Routes : NavKey {

    @Serializable
    data object Start : Routes()

    @Serializable
    data object Appoinments : Routes()

    @Serializable
    data object Meds : Routes()

    @Serializable
    data object MedsAddForm : Routes()

    @Serializable
    data object AddAppointmentForm : Routes()

    @Serializable
    data object AddExamForm : Routes()

    @Serializable
    data object Profile : Routes()


}