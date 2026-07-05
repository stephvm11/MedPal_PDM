package com.pdm0126.medpal.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.pdm0126.medpal.MedPalApplication
import com.pdm0126.medpal.data.repositories.repositoryAuth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel (
    private val repository: AuthRepository
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean?> = repository.isLoggedIn
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val userName: StateFlow<String?> = repository.userId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ){
        viewModelScope.launch{
            _error.value = null
            _error.value = null
            _isLoading.value = true

            try {
                repository.register(firstName,lastName,email,password)
                _successMessage.value = "Registro exitoso"
            }catch (e: Exception){
                val errorMessage = e.message ?: ""

                _error.value = when {
                    errorMessage.contains("user_already_exists", ignoreCase = true) ||
                            errorMessage.contains("already registered", ignoreCase = true) -> {
                        "Este correo electrónico ya está registrado. Intenta iniciar sesión."
                    }

                    errorMessage.contains("signup_disabled", ignoreCase = true) -> {
                        "El registro no está disponible temporalmente."
                    }
                    errorMessage.contains("weak_password", ignoreCase = true) ||
                            errorMessage.contains("should be at least", ignoreCase = true) -> {
                        "La contraseña es muy débil. Debe tener al menos 6 caracteres."
                    }

                    errorMessage.contains("unable to validate email address", ignoreCase = true) ||
                            errorMessage.contains("bad_request", ignoreCase = true) -> {
                        "El formato del correo electrónico no es válido."
                    }

                    errorMessage.contains("ConnectException", ignoreCase = true) ||
                            errorMessage.contains("UnknownHostException", ignoreCase = true) -> {
                        "No hay conexión a internet. Verifica tu red e inténtalo de nuevo."
                    }

                    errorMessage.contains("Error en el registro", ignoreCase = true) ||
                            errorMessage.contains("No se pudo obtener el id", ignoreCase = true) -> {
                        "No se pudo completar el registro. Verifica los datos ingresados."
                    }


                    else -> "Ocurrió un problema al crear la cuenta. Inténtalo de nuevo."
                }
            }

            _isLoading.value = false
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _error.value = null
            _successMessage.value = null
            _isLoading.value = true

            try {
                repository.login(email, password)
                _successMessage.value = "Login exitoso"
            } catch (e: Exception) {
                val errorMessage = e.message ?: ""

                _error.value = when {
                    errorMessage.contains("invalid_credentials", ignoreCase = true) -> {
                        "El correo o la contraseña son incorrectos. Por favor, verifica tus datos."
                    }

                    errorMessage.contains("unable to validate email address", ignoreCase = true) ||
                            errorMessage.contains("bad_request", ignoreCase = true) -> {
                        "El formato del correo electrónico no es válido."
                    }

                    errorMessage.contains("ConnectException", ignoreCase = true) ||
                            errorMessage.contains("UnknownHostException", ignoreCase = true) -> {
                        "No se pudo conectar al servidor. Revisa tu conexión a internet."
                    }

                    else -> "Ocurrió un problema al iniciar sesión. Inténtalo de nuevo."
                }

            }
            _isLoading.value = false
        }
    }

        fun logout() {
            viewModelScope.launch {
                try {
                    repository.logout()
                    _successMessage.value = "sesion cerrada"
                } catch (e: Exception) {
                    _error.value = e.message ?: "Error al cerrar sesion"
                }
            }
        }

        fun clearMessages() {
            _error.value = null
            _successMessage.value = null
        }

        fun setError(message: String) {
            _error.value = message
        }

        companion object {
            val Factory = viewModelFactory {
                initializer {
                    val app = this[APPLICATION_KEY] as MedPalApplication
                    AuthViewModel(app.appProvider.provideAuthRepository())
                }
            }
        }

}
