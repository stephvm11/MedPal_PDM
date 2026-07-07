package com.pdm0126.medpal.ui.screens.Profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.pdm0126.medpal.MedPalApplication
import com.pdm0126.medpal.data.model.User
import com.pdm0126.medpal.data.repositories.repositoryAuth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY

class ProfileViewModel (
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()


    init {
        loadUserData()
    }

    private fun loadUserData(){
        _error.value = null
        _isLoading.value = true
        viewModelScope.launch {
            try {
                authRepository.userId.collect { userId ->
                    val id = userId?.toLongOrNull()

                    if (id != null){
                        authRepository.getCurrentUser(id).collect { userData ->
                            if (userData != null){
                                _user.value = User(
                                    id = userData.id,
                                    authUserId = userData.authUserId,
                                    firstName = userData.firstName,
                                    lastName = userData.lastName,
                                )
                            } else {
                                _error.value = "No se encontro el perfil en la base de datos local"
                            }
                            _isLoading.value = false
                        }
                    } else{
                        _error.value = "Sesion invalidad o ID no encontrado"
                        _isLoading.value = false
                    }
                }
            } catch (e: Exception){
                _error.value = e.localizedMessage
                _isLoading.value = false
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as MedPalApplication
                val authRepo = app.appProvider.provideAuthRepository()

                ProfileViewModel(authRepository = authRepo)
            }
        }
    }

}