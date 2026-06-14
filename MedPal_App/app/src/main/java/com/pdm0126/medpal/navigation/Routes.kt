package com.pdm0126.medpal.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Routes : NavKey {

    @Serializable
    data object Home : Routes()

}