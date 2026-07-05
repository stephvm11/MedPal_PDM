package com.pdm0126.medpal.ui.screens.AddMed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.pdm0126.medpal.MedPalApplication
import com.pdm0126.medpal.data.local.database.entities.AdministrationRouteEntity
import com.pdm0126.medpal.data.model.AdministrationRoute
import com.pdm0126.medpal.data.repositories.repositoryAddMed.AddMedRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalTime

class AddMedicationViewModel(
    private val repository: AddMedRepository,
    private val userId: Long
) : ViewModel() {

    private val _routesList = MutableStateFlow<List<AdministrationRoute>>(emptyList())
    val routesList: StateFlow<List<AdministrationRoute>> = _routesList.asStateFlow()

    private val _event = MutableSharedFlow<String>()
    val event = _event.asSharedFlow()

    init {
        loadRoutesCatalog()
    }

    private fun loadRoutesCatalog() {
        viewModelScope.launch {
            repository.getAllRoutes().collect { models ->
                _routesList.value = models
            }
        }
    }

    /**
     * 🚀 Guarda secuencialmente el medicamento y opcionalmente su recordatorio,
     * enlazando la última dosis con el timestamp actual de la creación.
     */
    fun saveMedication(
        name: String,
        dosage: String,
        note: String?,
        selectedRouteName: String,
        isReminderEnabled: Boolean,
        reminderTime: LocalTime,
        selectedFrequency: String
    ) {
        viewModelScope.launch {
            val route = _routesList.value.find { it.route == selectedRouteName }

            if (route == null) {
                _event.emit("Por favor, selecciona una vía de administración válida.")
                return@launch
            }

            if (name.isBlank() || dosage.isBlank()) {
                _event.emit("El nombre y la dosis son campos obligatorios.")
                return@launch
            }

            repository.createMedication(
                name = name,
                dosage = dosage,
                notes = note.takeIf { it?.isNotBlank() == true },
                administrationRouteId = route.id,
                userId = userId
            ).onSuccess { medicationId ->

                if (isReminderEnabled) {
                    val frequencyDays = when (selectedFrequency) {
                        "Diario" -> 1
                        "Semanal" -> 7
                        "Quincenal" -> 15
                        "Mensual" -> 30
                        else -> 1 // "Personalizado" u otros por defecto actúan diario o según tu lógica
                    }

                    val formattedTime = "${reminderTime.hour.toString().padStart(2, '0')}:${
                        reminderTime.minute.toString().padStart(2, '0')
                    }:00"

                    repository.createReminder(
                        time = formattedTime,
                        frequencyDays = frequencyDays,
                        medicationId = medicationId
                    ).onSuccess {
                        _event.emit("¡Medicamento y recordatorio creados con éxito!")
                    }.onFailure { error ->
                        _event.emit("Medicamento creado, pero falló el recordatorio: ${error.localizedMessage}")
                    }
                } else {
                    _event.emit("¡Medicamento guardado correctamente!")
                }

            }.onFailure { error ->
                _event.emit("Error al guardar el medicamento: ${error.localizedMessage}")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as MedPalApplication

                val addMedRepository = app.appProvider.provideAddMedRepository()
                val authRepository = app.appProvider.provideAuthRepository()

                val currentUserId = runBlocking {
                    authRepository.userId.first()?.toLongOrNull() ?: 0L
                }

                AddMedicationViewModel(
                    repository = addMedRepository,
                    userId = currentUserId
                )
            }
        }
    }
}