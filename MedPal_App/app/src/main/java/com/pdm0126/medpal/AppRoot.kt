package com.pdm0126.medpal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pdm0126.medpal.ui.screens.auth.AuthViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.pdm0126.medpal.navigation.MedPal_App
import com.pdm0126.medpal.ui.screens.Splash.SplashScreen
import com.pdm0126.medpal.ui.screens.Start.StartScreen
import com.pdm0126.medpal.ui.screens.auth.LoginScreen

@Composable
fun AppRoot(
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
){

    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val userName by authViewModel.userName.collectAsState()

    var hasSeenStartScreen by rememberSaveable { mutableStateOf(false) }

    if (isLoggedIn == null){
        SplashScreen()
        return
    }

    if (!hasSeenStartScreen){
        StartScreen(
            onClick = {
                hasSeenStartScreen = true
            }
        )
    }else {
        when (isLoggedIn) {
            null -> SplashScreen()
            false -> LoginScreen()
            true -> MedPal_App(
                onLogout = { authViewModel.logout() }
            )
        }
    }

}