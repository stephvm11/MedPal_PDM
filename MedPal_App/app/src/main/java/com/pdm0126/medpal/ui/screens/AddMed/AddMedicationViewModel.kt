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
import com.pdm0126.medpal.data.model.TargetReminder
import com.pdm0126.medpal.data.repositories.repositoryAddMed.AddMedRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

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

    fun saveMedication(
        name: String,
        dosage: String,
        note: String?,
        selectedRouteName: String,
        isReminderEnabled: Boolean,
        reminderTime: LocalTime,
        selectedFrequency: String,
        startDate: LocalDate,
        remindersList: List<TargetReminder>
    ) {
        viewModelScope.launch {

            val actualDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            val route = _routesList.value.find { it.route == selectedRouteName }

            val daysDifference = actualDate.daysUntil(startDate)

            if (daysDifference < -30) {
                _event.emit("La fecha de inicio no puede ser mayor a 30 días en el pasado.")
                return@launch
            }

            if (daysDifference > 180) {
                _event.emit("No puedes programar un recordatorio con más de 6 meses de anticipación.")
                return@launch
            }


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

                if (isReminderEnabled && remindersList.isNotEmpty()) {
                    val frequencyDays = when (selectedFrequency) {
                        "Diario" -> 1
                        "Semanal" -> 7
                        "Quincenal" -> 15
                        "Mensual" -> 30
                        else -> 1
                    }

                    var succesReminders = true
                    var errorReminders = ""

                    remindersList.forEach {  target ->

                        val formattedTime = "${target.time.hour.toString().padStart(2, '0')}:${
                            target.time.minute.toString().padStart(2, '0')
                        }:00"

                        val formattedDateTime = "${startDate}T$formattedTime"

                        repository.createReminder(
                            time = formattedTime,
                        frequencyDays = frequencyDays,
                        medicationId = medicationId,
                        startDate = formattedDateTime
                        ).onFailure { error ->
                        succesReminders = false
                        errorReminders = error.localizedMessage ?: "Error desconocido"
                    }
                    }
                    if (succesReminders) {
                        _event.emit("¡Medicamento y todos sus recordatorios creados con éxito!")
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